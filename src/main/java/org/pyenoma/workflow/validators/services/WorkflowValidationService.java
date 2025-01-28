package org.pyenoma.workflow.validators.services;

import lombok.RequiredArgsConstructor;
import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.validators.workflowvalidators.IWorkflowValidator;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class WorkflowValidationService {
    private final List<IWorkflowValidator> validators;

    public void validate(Workflow workflow) {
        validators.forEach(validator -> validator.validate(workflow));
    }
}
