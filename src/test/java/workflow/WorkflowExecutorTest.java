package workflow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pyenoma.workflow.IWorkflowTask;
import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.WorkflowExecutor;
import org.pyenoma.workflow.WorkflowNodeResult;
import org.pyenoma.workflow.WorkflowRegistry;
import org.pyenoma.workflow.context.DefaultWorkflowContext;
import org.pyenoma.workflow.context.WorkflowContextFactory;
import org.pyenoma.workflow.exceptions.errorhandlers.DefaultWorkflowErrorHandler;
import org.springframework.context.ApplicationContext;
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

    @Mock private ApplicationContext applicationContext;

    @Mock private WorkflowContextFactory workflowContextFactory;

    @InjectMocks private WorkflowExecutor workflowExecutor;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testWorkflowNotFound() {
        assertDoesNotThrow(() -> workflowExecutor.execute("WorkflowId", DefaultWorkflowContext.class));
    }

    @Test
    void testThatWorkflowExecutesWithAllTasksSuccessfully() throws InterruptedException {
        // Setup
        Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency = new HashMap<>();
        adjacency.put(DummyWorkflowTask.class, Set.of(DummyWorkflowTask3.class));
        adjacency.put(DummyWorkflowTask2.class, Set.of(DummyWorkflowTask3.class));
        Workflow workflow = new Workflow(WORKFLOW_ID, adjacency);
        when(workflowRegistry.getWorkflow(WORKFLOW_ID)).thenReturn(workflow);
        DefaultWorkflowContext context = new DefaultWorkflowContext(WORKFLOW_ID);
        when(workflowContextFactory.createContext(DefaultWorkflowContext.class, WORKFLOW_ID)).thenReturn(context);
        DummyWorkflowTask dummyWorkflowTask = new DummyWorkflowTask();
        DummyWorkflowTask2 dummyWorkflowTask2 = new DummyWorkflowTask2();
        DummyWorkflowTask3 dummyWorkflowTask3 = new DummyWorkflowTask3();
        when(applicationContext.getBean(DummyWorkflowTask.class)).thenReturn(dummyWorkflowTask);
        when(applicationContext.getBean(DummyWorkflowTask2.class)).thenReturn(dummyWorkflowTask2);
        when(applicationContext.getBean(DummyWorkflowTask3.class)).thenReturn(dummyWorkflowTask3);
        // Act
        workflowExecutor.execute(WORKFLOW_ID, DefaultWorkflowContext.class);
        // Assert
        verify(workflowRegistry).getWorkflow(WORKFLOW_ID);
        verify(workflowContextFactory).createContext(DefaultWorkflowContext.class, WORKFLOW_ID);
        Iterator<Class<? extends IWorkflowTask>> executionOrderIterator = context.getExecutionOrder().keySet()
                .iterator();
        assertTrue(List.of(DummyWorkflowTask.class, DummyWorkflowTask2.class).contains(executionOrderIterator.next()));
        assertTrue(List.of(DummyWorkflowTask.class, DummyWorkflowTask2.class).contains(executionOrderIterator.next()));
        assertEquals(DummyWorkflowTask3.class, executionOrderIterator.next());
    }

    @Test
    void testThatWorkflowStopsWhenOneTaskFails() throws InterruptedException {
        // Setup
        Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency = new HashMap<>();
        adjacency.put(DummyWorkflowTask.class, Set.of(DummyWorkflowTask3.class));
        adjacency.put(DummyFailingWorkflowTask.class, Set.of(DummyWorkflowTask3.class));
        Workflow workflow = new Workflow(WORKFLOW_ID, adjacency);
        when(workflowRegistry.getWorkflow(WORKFLOW_ID)).thenReturn(workflow);
        DefaultWorkflowContext context = new DefaultWorkflowContext(WORKFLOW_ID);
        when(workflowContextFactory.createContext(DefaultWorkflowContext.class, WORKFLOW_ID)).thenReturn(context);
        DummyWorkflowTask dummyWorkflowTask = new DummyWorkflowTask();
        DummyFailingWorkflowTask dummyFailingWorkflowTask = new DummyFailingWorkflowTask();
        DummyWorkflowTask3 dummyWorkflowTask3 = new DummyWorkflowTask3();
        when(applicationContext.getBean(DummyWorkflowTask.class)).thenReturn(dummyWorkflowTask);
        when(applicationContext.getBean(DummyFailingWorkflowTask.class)).thenReturn(dummyFailingWorkflowTask);
        when(applicationContext.getBean(DummyWorkflowTask3.class)).thenReturn(dummyWorkflowTask3);
        // Act
        workflowExecutor.execute(WORKFLOW_ID, DefaultWorkflowContext.class);
        // Assert
        verify(workflowRegistry).getWorkflow(WORKFLOW_ID);
        verify(workflowContextFactory).createContext(DefaultWorkflowContext.class, WORKFLOW_ID);
        assertEquals(2, context.getExecutionOrder().size());
        Iterator<Class<? extends IWorkflowTask>> executionOrderIterator = context.getExecutionOrder().keySet()
                .iterator();
        assertTrue(List.of(DummyWorkflowTask.class, DummyFailingWorkflowTask.class)
                .contains(executionOrderIterator.next()));
        assertTrue(List.of(DummyWorkflowTask.class, DummyFailingWorkflowTask.class)
                .contains(executionOrderIterator.next()));
        assertEquals(WorkflowNodeResult.FAILURE, context.getExecutionOrder().get(DummyFailingWorkflowTask.class));
    }

    @Test
    void testThatWorkflowStopsWhenOneTaskThrowsException() throws InterruptedException {
        // Setup
        Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency = new HashMap<>();
        adjacency.put(DummyWorkflowTask.class, Set.of(DummyWorkflowTask3.class));
        adjacency.put(DummyWorkflowTaskThatThrowsException.class, Set.of(DummyWorkflowTask3.class));
        Workflow workflow = new Workflow(WORKFLOW_ID, adjacency);
        when(workflowRegistry.getWorkflow(WORKFLOW_ID)).thenReturn(workflow);
        DefaultWorkflowContext context = new DefaultWorkflowContext(WORKFLOW_ID);
        when(workflowContextFactory.createContext(DefaultWorkflowContext.class, WORKFLOW_ID)).thenReturn(context);
        DummyWorkflowTask dummyWorkflowTask = new DummyWorkflowTask();
        DummyWorkflowTaskThatThrowsException dummyWorkflowTaskThatThrowsException = new DummyWorkflowTaskThatThrowsException();
        DummyWorkflowTask3 dummyWorkflowTask3 = new DummyWorkflowTask3();
        when(applicationContext.getBean(DummyWorkflowTask.class)).thenReturn(dummyWorkflowTask);
        when(applicationContext.getBean(DummyWorkflowTaskThatThrowsException.class)).thenReturn(
                dummyWorkflowTaskThatThrowsException);
        when(applicationContext.getBean(DummyWorkflowTask3.class)).thenReturn(dummyWorkflowTask3);
        when(applicationContext.getBean(DefaultWorkflowErrorHandler.class)).thenReturn(
                new DefaultWorkflowErrorHandler());
        // Act
        workflowExecutor.execute(WORKFLOW_ID, DefaultWorkflowContext.class);
        // Assert
        verify(workflowRegistry).getWorkflow(WORKFLOW_ID);
        verify(workflowContextFactory).createContext(DefaultWorkflowContext.class, WORKFLOW_ID);
        assertEquals(2, context.getExecutionOrder().size());
        Iterator<Class<? extends IWorkflowTask>> executionOrderIterator = context.getExecutionOrder().keySet()
                .iterator();
        assertTrue(List.of(DummyWorkflowTask.class, DummyWorkflowTaskThatThrowsException.class)
                .contains(executionOrderIterator.next()));
        assertTrue(List.of(DummyWorkflowTask.class, DummyWorkflowTaskThatThrowsException.class)
                .contains(executionOrderIterator.next()));
        assertEquals(WorkflowNodeResult.FAILURE,
                context.getExecutionOrder().get(DummyWorkflowTaskThatThrowsException.class));
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

}