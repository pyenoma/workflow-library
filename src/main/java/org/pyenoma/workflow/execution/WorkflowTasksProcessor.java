package org.pyenoma.workflow.execution;

import lombok.extern.log4j.Log4j2;
import org.pyenoma.workflow.IWorkflowTask;
import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.WorkflowNodeResult;
import org.pyenoma.workflow.annotations.WorkflowTaskBean;
import org.pyenoma.workflow.context.IWorkflowContext;
import org.pyenoma.workflow.exceptions.WorkflowException;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class WorkflowTasksProcessor {

    private final IWorkflowContext context;

    private final Executor executor;

    private final ApplicationContext applicationContext;

    private final Workflow workflow;

    private final Map<Class<? extends IWorkflowTask>, Integer> inDegree;

    private final BlockingQueue<Class<? extends IWorkflowTask>> readyQueue;

    private final CountDownLatch latch;

    private final AtomicBoolean stopExecution = new AtomicBoolean(false);

    public WorkflowTasksProcessor(Workflow workflow, IWorkflowContext context, Executor executor,
            ApplicationContext applicationContext) throws InterruptedException {
        this.context = context;
        this.executor = executor;
        this.applicationContext = applicationContext;
        this.workflow = workflow;
        Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency = workflow.adjacency();
        inDegree = calculateInDegree(adjacency);
        readyQueue = initializeReadyQueue(inDegree);
        latch = new CountDownLatch(adjacency.entrySet().stream()
                .flatMap(entry -> Stream.concat(Stream.of(entry.getKey()), entry.getValue().stream()))
                .collect(Collectors.toSet()).size());
    }

    public void process() throws InterruptedException {
        processTasks();
        latch.await();
    }

    private void processTasks() throws InterruptedException {
        while (latch.getCount() > 0 && !stopExecution.get()) {
            Class<? extends IWorkflowTask> taskClass = readyQueue.poll(500, TimeUnit.MILLISECONDS);
            if (taskClass != null) {
                executeTask(taskClass);
            }
        }
        if (stopExecution.get()) {
            while (latch.getCount() > 0) {
                latch.countDown();
            }
            log.warn("Workflow {} execution stopped.", context.getWorkflowId());
        }
    }

    private void executeTask(Class<? extends IWorkflowTask> taskClass) {
        executor.execute(() -> {
            IWorkflowTask taskInstance = applicationContext.getBean(taskClass);
            WorkflowNodeResult executionResult = WorkflowNodeResult.FAILURE;
            try {
                executionResult = taskInstance.execute(context);
                context.addExecution(taskClass, executionResult);
                if (WorkflowNodeResult.FAILURE.equals(executionResult)) {
                    log.warn("Stopping workflow: {} due to task: {}, Execution result: {}", context.getWorkflowId(),
                            taskClass.getSimpleName(), executionResult);
                    stopExecution.set(true);
                }
            } catch (WorkflowException e) {
                applicationContext.getBean(taskClass.getAnnotation(WorkflowTaskBean.class).errorHandler())
                        .handle(e, context, taskClass);
                stopExecution.set(true);
                log.error("Task execution failed for {}: {}", taskClass.getSimpleName(), e.getMessage(), e);
            } finally {
                handleTaskCompletion(taskClass, executionResult);
            }
        });
    }

    private void handleTaskCompletion(Class<? extends IWorkflowTask> taskClass, WorkflowNodeResult executionResult) {
        latch.countDown();
        Set<Class<? extends IWorkflowTask>> successors = workflow.adjacency().get(taskClass);
        if (successors != null && WorkflowNodeResult.SUCCESS.equals(executionResult)) {
            for (Class<? extends IWorkflowTask> successor : successors) {
                Integer newVal = inDegree.computeIfPresent(successor, (_, oldVal) -> oldVal - 1);
                if (Integer.valueOf(0).equals(newVal)) {
                    try {
                        readyQueue.put(successor);
                    } catch (InterruptedException e) {
                        log.error("Error adding task to ready queue: {}", e.getMessage(), e);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    private Map<Class<? extends IWorkflowTask>, Integer> calculateInDegree(
            Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency) {
        Map<Class<? extends IWorkflowTask>, Integer> inDegree = new ConcurrentHashMap<>();
        Set<Class<? extends IWorkflowTask>> allTasks = adjacency.entrySet().stream()
                .flatMap(entry -> Stream.concat(Stream.of(entry.getKey()), entry.getValue().stream()))
                .collect(Collectors.toSet());
        allTasks.forEach(taskClass -> inDegree.put(taskClass, 0));

        adjacency.forEach((_, successors) -> {
            if (successors != null) {
                for (Class<? extends IWorkflowTask> successor : successors) {
                    inDegree.computeIfPresent(successor, (_, oldVal) -> oldVal + 1);
                }
            }
        });

        return inDegree;
    }

    private BlockingQueue<Class<? extends IWorkflowTask>> initializeReadyQueue(
            Map<Class<? extends IWorkflowTask>, Integer> inDegree) throws InterruptedException {
        BlockingQueue<Class<? extends IWorkflowTask>> readyQueue = new LinkedBlockingQueue<>();
        for (Map.Entry<Class<? extends IWorkflowTask>, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                readyQueue.put(entry.getKey());
            }
        }
        return readyQueue;
    }

}
