package workflow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pyenoma.workflow.IWorkflowTask;
import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.WorkflowBuilder;
import org.pyenoma.workflow.WorkflowRegistry;
import org.pyenoma.workflow.annotations.WorkflowDefinition;
import org.pyenoma.workflow.context.DefaultWorkflowContext;
import org.pyenoma.workflow.exceptions.WorkflowException;
import org.pyenoma.workflow.validators.services.WorkflowDefinitionValidationService;
import org.pyenoma.workflow.validators.services.WorkflowValidationService;
import org.springframework.context.ApplicationContext;
import workflow.dummies.DummyWorkflowDefinitionBeanWithId;
import workflow.dummies.DummyWorkflowDefinitionBeanWithNoId;
import workflow.dummies.DummyWorkflowTask;
import workflow.dummies.DummyWorkflowTask2;
import workflow.dummies.DummyWorkflowTask3;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WorkflowBuilderTest {

    @Mock private ApplicationContext applicationContext;

    @Mock private WorkflowDefinitionValidationService workflowDefinitionValidationService;

    @Mock private WorkflowValidationService workflowValidationService;

    @Mock private WorkflowRegistry workflowRegistry;

    @InjectMocks private WorkflowBuilder workflowBuilder;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuildRegistersValidWorkflowsWithNoIdWorkflowDefinition() throws WorkflowException {
        // Arrange
        when(applicationContext.getBeanNamesForAnnotation(WorkflowDefinition.class)).thenReturn(
                new String[] {DummyWorkflowDefinitionBeanWithNoId.class.getName()});
        when(applicationContext.getType(DummyWorkflowDefinitionBeanWithNoId.class.getName())).thenAnswer(
                invocation -> DummyWorkflowDefinitionBeanWithNoId.class);
        doNothing().when(workflowDefinitionValidationService).validate(anyString(), any());
        doNothing().when(workflowValidationService).validate(any());

        // Act
        workflowBuilder.build();

        // Assert
        @SuppressWarnings("unchecked") ArgumentCaptor<Workflow<DefaultWorkflowContext>> workflowCaptor = (ArgumentCaptor<Workflow<DefaultWorkflowContext>>) (ArgumentCaptor<?>) ArgumentCaptor.forClass(
                Workflow.class);

        verify(workflowRegistry).register(eq(DummyWorkflowDefinitionBeanWithNoId.class.getName()),
                workflowCaptor.capture());
        Workflow<DefaultWorkflowContext> workflow = workflowCaptor.getValue();
        assertNotNull(workflow);
        Map<Class<? extends IWorkflowTask<DefaultWorkflowContext>>, Set<Class<? extends IWorkflowTask<DefaultWorkflowContext>>>> adjacency = workflow.adjacency();
        assertEquals(2, adjacency.size());
        assertTrue(adjacency.containsKey(DummyWorkflowTask.class));
        assertTrue(adjacency.containsKey(DummyWorkflowTask2.class));
        assertTrue(adjacency.get(DummyWorkflowTask.class).contains(DummyWorkflowTask3.class));
        assertTrue(adjacency.get(DummyWorkflowTask2.class).contains(DummyWorkflowTask3.class));
    }

    @Test
    void testBuildRegistersValidWorkflowsWithWorkflowDefinitionWithId() throws WorkflowException {
        // Arrange
        when(applicationContext.getBeanNamesForAnnotation(WorkflowDefinition.class)).thenReturn(
                new String[] {DummyWorkflowDefinitionBeanWithId.class.getName()});
        when(applicationContext.getType(DummyWorkflowDefinitionBeanWithId.class.getName())).thenAnswer(
                invocation -> DummyWorkflowDefinitionBeanWithId.class);
        doNothing().when(workflowDefinitionValidationService).validate(anyString(), any());
        doNothing().when(workflowValidationService).validate(any());

        // Act
        workflowBuilder.build();

        // Assert
        @SuppressWarnings("unchecked") ArgumentCaptor<Workflow<DefaultWorkflowContext>> workflowCaptor = (ArgumentCaptor<Workflow<DefaultWorkflowContext>>) (ArgumentCaptor<?>) ArgumentCaptor.forClass(
                Workflow.class);

        verify(workflowRegistry).register(
                eq(DummyWorkflowDefinitionBeanWithId.class.getAnnotation(WorkflowDefinition.class).id()),
                workflowCaptor.capture());
        Workflow<DefaultWorkflowContext> workflow = workflowCaptor.getValue();
        assertNotNull(workflow);
        Map<Class<? extends IWorkflowTask<DefaultWorkflowContext>>, Set<Class<? extends IWorkflowTask<DefaultWorkflowContext>>>> adjacency = workflow.adjacency();
        assertEquals(2, adjacency.size());
        assertTrue(adjacency.containsKey(DummyWorkflowTask.class));
        assertTrue(adjacency.containsKey(DummyWorkflowTask2.class));
        assertTrue(adjacency.get(DummyWorkflowTask.class).contains(DummyWorkflowTask3.class));
        assertTrue(adjacency.get(DummyWorkflowTask2.class).contains(DummyWorkflowTask3.class));
    }

    @Test
    void testBuildThrowsWorkflowException() throws WorkflowException {
        // Arrange
        when(applicationContext.getBeanNamesForAnnotation(WorkflowDefinition.class)).thenReturn(
                new String[] {DummyWorkflowDefinitionBeanWithNoId.class.getName()});
        when(applicationContext.getType(DummyWorkflowDefinitionBeanWithNoId.class.getName())).thenAnswer(
                invocation -> DummyWorkflowDefinitionBeanWithNoId.class);
        doThrow(new WorkflowException("WorkflowId", "Invalid task class definition found")).when(
                        workflowDefinitionValidationService)
                .validate(anyString(), any());
        doNothing().when(workflowValidationService).validate(any());

        // Act and assert
        assertThrows(RuntimeException.class, () -> workflowBuilder.build());

    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

}