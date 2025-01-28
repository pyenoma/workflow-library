package workflow.validators.workflowvalidators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.exceptions.CyclicWorkflowDefinedException;
import org.pyenoma.workflow.validators.workflowvalidators.CycleValidator;
import workflow.dummies.DummyWorkflowTask;
import workflow.dummies.DummyWorkflowTask2;
import workflow.dummies.DummyWorkflowTask3;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CycleValidatorTest {

    private static final String WORKFLOW_ID = "WorkflowId";

    private CycleValidator cycleValidator;

    @BeforeEach
    void setUp() {
        cycleValidator = new CycleValidator();
    }

    @Test
    void validateCycleIsPresent() {
        // Arrange
        Workflow workflow = new Workflow(WORKFLOW_ID,
                Map.of(DummyWorkflowTask.class, Set.of(DummyWorkflowTask2.class), DummyWorkflowTask2.class,
                        Set.of(DummyWorkflowTask3.class), DummyWorkflowTask3.class, Set.of(DummyWorkflowTask.class)));
        // Act
        assertThrows(CyclicWorkflowDefinedException.class, () -> cycleValidator.validate(workflow));
    }

    @Test
    void validateCycleIsNotPresent() {
        // Arrange
        Workflow workflow = new Workflow(WORKFLOW_ID,
                Map.of(DummyWorkflowTask.class, Set.of(DummyWorkflowTask2.class, DummyWorkflowTask3.class),
                        DummyWorkflowTask2.class, Set.of(DummyWorkflowTask3.class)));
        // Act
        assertDoesNotThrow(() -> cycleValidator.validate(workflow));
    }
}