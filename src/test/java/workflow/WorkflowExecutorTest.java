package workflow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pyenoma.workflow.IWorkflowTask;
import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.WorkflowNodeResult;
import org.pyenoma.workflow.WorkflowRegistry;
import org.pyenoma.workflow.context.DefaultWorkflowContext;
import org.pyenoma.workflow.context.IWorkflowContext;
import org.pyenoma.workflow.exceptions.errorhandlers.DefaultWorkflowErrorHandler;
import org.pyenoma.workflow.execution.WorkflowExecutor;
import org.pyenoma.workflow.execution.WorkflowTasksProcessor;
import org.pyenoma.workflow.execution.WorkflowTasksProcessorFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import workflow.dummies.DummyFailingWorkflowTask;
import workflow.dummies.DummyWorkflowTask;
import workflow.dummies.DummyWorkflowTask2;
import workflow.dummies.DummyWorkflowTask3;
import workflow.dummies.DummyWorkflowTaskThatThrowsException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WorkflowExecutorTest {

    private static final String WORKFLOW_ID = "WorkflowId";

    @Mock private WorkflowRegistry workflowRegistry;

    ThreadPoolTaskExecutor executor;

    @Mock private ApplicationContext applicationContext;

    @InjectMocks private WorkflowExecutor workflowExecutor;

    private AutoCloseable mocks;

    @Mock private WorkflowTasksProcessorFactory workflowTasksProcessorFactory;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        executor = new ThreadPoolTaskExecutor();
        executor.initialize();
    }

    @Test
    void testWorkflowNotFound() {
        assertDoesNotThrow(
                () -> workflowExecutor.execute("WorkflowId", () -> new DefaultWorkflowContext("WorkflowId")));
    }

    @Test
    void testThatWorkflowExecutesWithAllTasksSuccessfully() throws InterruptedException {
        // Setup
        Map<Class<? extends IWorkflowTask<IWorkflowContext>>, Set<Class<? extends IWorkflowTask<IWorkflowContext>>>> adjacency = new HashMap<>();
        adjacency.put(DummyWorkflowTask.class, Set.of(DummyWorkflowTask3.class));
        adjacency.put(DummyWorkflowTask2.class, Set.of(DummyWorkflowTask3.class));
        Workflow<IWorkflowContext> workflow = new Workflow<>(WORKFLOW_ID, adjacency);
        when(workflowRegistry.getWorkflow(WORKFLOW_ID)).thenAnswer(_ -> workflow);
        DefaultWorkflowContext context = new DefaultWorkflowContext(WORKFLOW_ID);
        DummyWorkflowTask dummyWorkflowTask = new DummyWorkflowTask();
        DummyWorkflowTask2 dummyWorkflowTask2 = new DummyWorkflowTask2();
        DummyWorkflowTask3 dummyWorkflowTask3 = new DummyWorkflowTask3();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        when(workflowTasksProcessorFactory.create(workflow, context)).thenReturn(
                new WorkflowTasksProcessor<>(workflow, context, executor, applicationContext));
        when(applicationContext.getBean(DummyWorkflowTask.class)).thenReturn(dummyWorkflowTask);
        when(applicationContext.getBean(DummyWorkflowTask2.class)).thenReturn(dummyWorkflowTask2);
        when(applicationContext.getBean(DummyWorkflowTask3.class)).thenReturn(dummyWorkflowTask3);
        // Act
        workflowExecutor.execute(WORKFLOW_ID, () -> context);
        // Assert
        verify(workflowRegistry).getWorkflow(WORKFLOW_ID);
        Iterator<Class<? extends IWorkflowTask<?>>> executionOrderIterator = context.getExecutions().keySet()
                .iterator();
        assertTrue(List.of(DummyWorkflowTask.class, DummyWorkflowTask2.class).contains(executionOrderIterator.next()));
        assertTrue(List.of(DummyWorkflowTask.class, DummyWorkflowTask2.class).contains(executionOrderIterator.next()));
        assertEquals(DummyWorkflowTask3.class, executionOrderIterator.next());
    }

    @Test
    void testThatWorkflowStopsWhenOneTaskFails() throws InterruptedException {
        // Setup
        Map<Class<? extends IWorkflowTask<IWorkflowContext>>, Set<Class<? extends IWorkflowTask<IWorkflowContext>>>> adjacency = new HashMap<>();
        adjacency.put(DummyWorkflowTask.class, Set.of(DummyWorkflowTask3.class));
        adjacency.put(DummyFailingWorkflowTask.class, Set.of(DummyWorkflowTask3.class));
        Workflow<IWorkflowContext> workflow = new Workflow<>(WORKFLOW_ID, adjacency);
        when(workflowRegistry.getWorkflow(WORKFLOW_ID)).thenAnswer(_ -> workflow);
        DefaultWorkflowContext context = new DefaultWorkflowContext(WORKFLOW_ID);
        DummyWorkflowTask dummyWorkflowTask = new DummyWorkflowTask();
        DummyFailingWorkflowTask dummyFailingWorkflowTask = new DummyFailingWorkflowTask();
        DummyWorkflowTask3 dummyWorkflowTask3 = new DummyWorkflowTask3();
        when(workflowTasksProcessorFactory.create(workflow, context)).thenReturn(
                new WorkflowTasksProcessor<>(workflow, context, executor, applicationContext));
        when(applicationContext.getBean(DummyWorkflowTask.class)).thenReturn(dummyWorkflowTask);
        when(applicationContext.getBean(DummyFailingWorkflowTask.class)).thenReturn(dummyFailingWorkflowTask);
        when(applicationContext.getBean(DummyWorkflowTask3.class)).thenReturn(dummyWorkflowTask3);
        // Act
        workflowExecutor.execute(WORKFLOW_ID, () -> context);
        // Assert
        verify(workflowRegistry).getWorkflow(WORKFLOW_ID);
        assertEquals(2, context.getExecutions().size());
        Iterator<Class<? extends IWorkflowTask<?>>> executionOrderIterator = context.getExecutions().keySet()
                .iterator();
        assertTrue(List.of(DummyWorkflowTask.class, DummyFailingWorkflowTask.class)
                .contains(executionOrderIterator.next()));
        assertTrue(List.of(DummyWorkflowTask.class, DummyFailingWorkflowTask.class)
                .contains(executionOrderIterator.next()));
        assertEquals(WorkflowNodeResult.FAILURE, context.getExecutions().get(DummyFailingWorkflowTask.class));
    }

    @Test
    void testThatWorkflowStopsWhenOneTaskThrowsException() throws InterruptedException {
        // Setup
        Map<Class<? extends IWorkflowTask<IWorkflowContext>>, Set<Class<? extends IWorkflowTask<IWorkflowContext>>>> adjacency = new HashMap<>();
        adjacency.put(DummyWorkflowTask.class, Set.of(DummyWorkflowTask3.class));
        adjacency.put(DummyWorkflowTaskThatThrowsException.class, Set.of(DummyWorkflowTask3.class));
        Workflow<IWorkflowContext> workflow = new Workflow<>(WORKFLOW_ID, adjacency);
        when(workflowRegistry.getWorkflow(WORKFLOW_ID)).thenAnswer(_ -> workflow);
        DefaultWorkflowContext context = new DefaultWorkflowContext(WORKFLOW_ID);
        DummyWorkflowTask dummyWorkflowTask = new DummyWorkflowTask();
        DummyWorkflowTaskThatThrowsException dummyWorkflowTaskThatThrowsException = new DummyWorkflowTaskThatThrowsException();
        DummyWorkflowTask3 dummyWorkflowTask3 = new DummyWorkflowTask3();
        when(workflowTasksProcessorFactory.create(workflow, context)).thenReturn(
                new WorkflowTasksProcessor<>(workflow, context, executor, applicationContext));
        when(applicationContext.getBean(DummyWorkflowTask.class)).thenReturn(dummyWorkflowTask);
        when(applicationContext.getBean(DummyWorkflowTaskThatThrowsException.class)).thenReturn(
                dummyWorkflowTaskThatThrowsException);
        when(applicationContext.getBean(DummyWorkflowTask3.class)).thenReturn(dummyWorkflowTask3);
        when(applicationContext.getBean(DefaultWorkflowErrorHandler.class)).thenReturn(
                new DefaultWorkflowErrorHandler());
        // Act
        workflowExecutor.execute(WORKFLOW_ID, () -> context);
        // Assert
        verify(workflowRegistry).getWorkflow(WORKFLOW_ID);
        assertEquals(2, context.getExecutions().size());
        Iterator<Class<? extends IWorkflowTask<?>>> executionOrderIterator = context.getExecutions().keySet()
                .iterator();
        assertTrue(List.of(DummyWorkflowTask.class, DummyWorkflowTaskThatThrowsException.class)
                .contains(executionOrderIterator.next()));
        assertTrue(List.of(DummyWorkflowTask.class, DummyWorkflowTaskThatThrowsException.class)
                .contains(executionOrderIterator.next()));
        assertEquals(WorkflowNodeResult.FAILURE,
                context.getExecutions().get(DummyWorkflowTaskThatThrowsException.class));
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

}