package org.pyenoma.workflow;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pyenoma.workflow.annotations.WorkflowTaskBean;
import org.pyenoma.workflow.context.IWorkflowContext;
import org.pyenoma.workflow.context.WorkflowContextFactory;
import org.pyenoma.workflow.exceptions.WorkflowException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RequiredArgsConstructor
@Service
public class WorkflowExecutor {

    private final WorkflowRegistry workflowRegistry;

    private final ApplicationContext applicationContext;

    private final WorkflowContextFactory workflowContextFactory;

    private final ExecutorService executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());

    private final AtomicBoolean stopExecution = new AtomicBoolean(false);

    public void execute(@NonNull String workflowId, Class<? extends IWorkflowContext> workflowContextClass)
            throws InterruptedException, WorkflowException {
        Workflow workflow = workflowRegistry.getWorkflow(workflowId);
        if (workflow == null) {
            log.error("No workflow found with ID: {}", workflowId);
            return;
        }
        IWorkflowContext context = workflowContextFactory.createContext(workflowContextClass, workflowId);
        log.info("Starting execution of workflow with ID: {}", workflowId);

        Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency = workflow.adjacency();
        Map<Class<? extends IWorkflowTask>, Integer> inDegree = calculateInDegree(adjacency);
        BlockingQueue<Class<? extends IWorkflowTask>> readyQueue = initializeReadyQueue(inDegree);

        CountDownLatch latch = new CountDownLatch(adjacency.entrySet().stream()
                .flatMap(entry -> Stream.concat(Stream.of(entry.getKey()), entry.getValue().stream()))
                .collect(Collectors.toSet()).size());
        processTasks(context, adjacency, inDegree, readyQueue, latch);

        latch.await();
        if (!stopExecution.get()) {
            log.info("Workflow {} execution completed successfully!", workflowId);
        } else {
            log.warn("Workflow {} execution stopped due to task failure!", workflowId);
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

    private void processTasks(IWorkflowContext context,
            Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency,
            Map<Class<? extends IWorkflowTask>, Integer> inDegree,
            BlockingQueue<Class<? extends IWorkflowTask>> readyQueue, CountDownLatch latch)
            throws InterruptedException {

        while (latch.getCount() > 0 && !stopExecution.get()) {
            Class<? extends IWorkflowTask> taskClass = readyQueue.poll(500, TimeUnit.MILLISECONDS);
            if (taskClass != null) {
                executeTask(context, adjacency, inDegree, readyQueue, latch, taskClass);
            }
        }
        if (stopExecution.get()) {
            while (latch.getCount() > 0) {
                latch.countDown();
            }
            log.warn("Workflow {} execution stopped.", context.getWorkflowId());
        }
    }

    private void executeTask(IWorkflowContext context,
            Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency,
            Map<Class<? extends IWorkflowTask>, Integer> inDegree,
            BlockingQueue<Class<? extends IWorkflowTask>> readyQueue, CountDownLatch latch,
            Class<? extends IWorkflowTask> taskClass) {
        executorService.submit(() -> {
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
                handleTaskCompletion(adjacency, inDegree, readyQueue, latch, taskClass, executionResult);
            }
        });
    }

    private void handleTaskCompletion(
            Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency,
            Map<Class<? extends IWorkflowTask>, Integer> inDegree,
            BlockingQueue<Class<? extends IWorkflowTask>> readyQueue, CountDownLatch latch,
            Class<? extends IWorkflowTask> taskClass, WorkflowNodeResult executionResult) {
        latch.countDown();
        Set<Class<? extends IWorkflowTask>> successors = adjacency.get(taskClass);
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

}
