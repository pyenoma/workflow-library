package org.pyenoma.workflow.exceptions;

import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.context.IWorkflowContext;

public class CyclicWorkflowDefinedException extends WorkflowException {
    public CyclicWorkflowDefinedException(Workflow<? extends IWorkflowContext> workflow) {
        super(buildMessage(workflow));
    }

    private static String buildMessage(Workflow<? extends IWorkflowContext> workflow) {
        return "Cyclic workflow definition found | " + "Workflow: " + workflow.toString();
    }
}
