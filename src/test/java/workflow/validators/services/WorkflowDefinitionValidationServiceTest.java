package workflow.validators.services;

import org.junit.jupiter.api.Test;
import org.pyenoma.workflow.validators.services.WorkflowDefinitionValidationService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class WorkflowDefinitionValidationServiceTest {

    @Test
    void validate() {
        WorkflowDefinitionValidationService service = new WorkflowDefinitionValidationService(
                Collections.singletonList((id, clazz) -> {
                    // do nothing
                }));
        assertDoesNotThrow(() -> service.validate("workflowId", Object.class));
    }
}