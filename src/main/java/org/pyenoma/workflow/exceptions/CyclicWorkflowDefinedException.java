package org.pyenoma.workflow.exceptions;

import org.pyenoma.workflow.Workflow;

public class CyclicWorkflowDefinedException extends WorkflowException {
    public CyclicWorkflowDefinedException(Workflow workflow) {
        super(buildMessage(workflow));
    }

    private static String buildMessage(Workflow workflow) {
        return "Cyclic workflow definition found | " + "Workflow: " + workflow.toString();
    }
}
