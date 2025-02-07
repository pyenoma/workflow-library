package org.pyenoma.workflow.validators.services;

import lombok.RequiredArgsConstructor;
import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.context.IWorkflowContext;
import org.pyenoma.workflow.exceptions.WorkflowException;
import org.pyenoma.workflow.validators.workflowvalidators.IWorkflowValidator;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class WorkflowValidationService {
    private final List<IWorkflowValidator> validators;

    public void validate(Workflow<? extends IWorkflowContext> workflow) throws WorkflowException {
        for (IWorkflowValidator validator : validators) {
            validator.validate(workflow);
        }
    }
}
