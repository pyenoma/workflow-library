package workflow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import workflow.annotations.WorkflowDefinition;
import workflow.dummies.DummyWorkflowDefinitionBeanWithId;
import workflow.dummies.DummyWorkflowDefinitionBeanWithNoId;
import workflow.dummies.DummyWorkflowTask;
import workflow.dummies.DummyWorkflowTask2;
import workflow.dummies.DummyWorkflowTask3;
import workflow.exceptions.WorkflowException;
import workflow.validators.services.WorkflowDefinitionValidationService;

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

    @Mock private WorkflowDefinitionValidationService validationService;

    @Mock private WorkflowRegistry workflowRegistry;

    @InjectMocks private WorkflowBuilder workflowBuilder;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuildRegistersValidWorkflowsWithNoIdWorkflowDefinition() {
        // Arrange
        when(applicationContext.getBeanNamesForAnnotation(WorkflowDefinition.class)).thenReturn(
                new String[] {DummyWorkflowDefinitionBeanWithNoId.class.getName()});
        when(applicationContext.getType(DummyWorkflowDefinitionBeanWithNoId.class.getName())).thenAnswer(
                _ -> DummyWorkflowDefinitionBeanWithNoId.class);
        doNothing().when(validationService).validate(anyString(), any());

        // Act
        workflowBuilder.build();

        // Assert
        ArgumentCaptor<Workflow> workflowCaptor = ArgumentCaptor.forClass(Workflow.class);
        verify(workflowRegistry).register(eq(DummyWorkflowDefinitionBeanWithNoId.class.getName()),
                workflowCaptor.capture());
        Workflow workflow = workflowCaptor.getValue();
        assertNotNull(workflow);
        Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency = workflow.adjacency();
        assertEquals(2, adjacency.size());
        assertTrue(adjacency.containsKey(DummyWorkflowTask.class));
        assertTrue(adjacency.containsKey(DummyWorkflowTask2.class));
        assertTrue(adjacency.get(DummyWorkflowTask.class).contains(DummyWorkflowTask3.class));
        assertTrue(adjacency.get(DummyWorkflowTask2.class).contains(DummyWorkflowTask3.class));
    }

    @Test
    void testBuildRegistersValidWorkflowsWithWorkflowDefinitionWithId() {
        // Arrange
        when(applicationContext.getBeanNamesForAnnotation(WorkflowDefinition.class)).thenReturn(
                new String[] {DummyWorkflowDefinitionBeanWithId.class.getName()});
        when(applicationContext.getType(DummyWorkflowDefinitionBeanWithId.class.getName())).thenAnswer(
                _ -> DummyWorkflowDefinitionBeanWithId.class);
        doNothing().when(validationService).validate(anyString(), any());

        // Act
        workflowBuilder.build();

        // Assert
        ArgumentCaptor<Workflow> workflowCaptor = ArgumentCaptor.forClass(Workflow.class);
        verify(workflowRegistry).register(
                eq(DummyWorkflowDefinitionBeanWithId.class.getAnnotation(WorkflowDefinition.class).id()),
                workflowCaptor.capture());
        Workflow workflow = workflowCaptor.getValue();
        assertNotNull(workflow);
        Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency = workflow.adjacency();
        assertEquals(2, adjacency.size());
        assertTrue(adjacency.containsKey(DummyWorkflowTask.class));
        assertTrue(adjacency.containsKey(DummyWorkflowTask2.class));
        assertTrue(adjacency.get(DummyWorkflowTask.class).contains(DummyWorkflowTask3.class));
        assertTrue(adjacency.get(DummyWorkflowTask2.class).contains(DummyWorkflowTask3.class));
    }

    @Test
    void testBuildThrowsWorkflowException() {
        // Arrange
        when(applicationContext.getBeanNamesForAnnotation(WorkflowDefinition.class)).thenReturn(
                new String[] {DummyWorkflowDefinitionBeanWithNoId.class.getName()});
        when(applicationContext.getType(DummyWorkflowDefinitionBeanWithNoId.class.getName())).thenAnswer(
                _ -> DummyWorkflowDefinitionBeanWithNoId.class);
        doThrow(new WorkflowException("Invalid task class definition found")).when(validationService)
                .validate(anyString(), any());

        // Act and assert
        assertThrows(WorkflowException.class, () -> workflowBuilder.build());

    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

}