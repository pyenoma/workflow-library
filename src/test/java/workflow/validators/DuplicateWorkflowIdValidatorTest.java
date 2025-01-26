package workflow.validators;

import org.junit.jupiter.api.Test;
import workflow.dummies.DummyWorkflowDefinitionBeanWithNoId;
import workflow.exceptions.DuplicateWorkflowIdException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DuplicateWorkflowIdValidatorTest {
    @Test
    void validateSuccess() {
        Set<String> workflowIds = new HashSet<>();
        assertDoesNotThrow(() -> new DuplicateWorkflowIdValidator(workflowIds).validate("WorkflowId",
                DummyWorkflowDefinitionBeanWithNoId.class));
    }

    @Test
    void validateFailure() {
        Set<String> workflowIds = new HashSet<>();
        workflowIds.add("WorkflowId");

        IWorkflowDefinitionValidator duplicateWorkflowIdValidator = new DuplicateWorkflowIdValidator(workflowIds);
        assertThrows(DuplicateWorkflowIdException.class, () -> {
            duplicateWorkflowIdValidator.validate("WorkflowId", DummyWorkflowDefinitionBeanWithNoId.class);
        });
    }
}