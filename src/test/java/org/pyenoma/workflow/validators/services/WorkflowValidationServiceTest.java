package org.pyenoma.workflow.validators.services;

import org.junit.jupiter.api.Test;
import org.pyenoma.workflow.Workflow;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class WorkflowValidationServiceTest {

    @Test
    void validate() {
        WorkflowValidationService service = new WorkflowValidationService(Collections.singletonList(_ -> {
            // do nothing
        }));
        assertDoesNotThrow(() -> service.validate(new Workflow<>("workflowId", Collections.emptyMap())));
    }
}