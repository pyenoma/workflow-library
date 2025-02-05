package org.pyenoma.workflow.validators.workflowvalidators;

import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.context.IWorkflowContext;

public interface IWorkflowValidator {
    void validate(Workflow<? extends IWorkflowContext> workflow);
}
