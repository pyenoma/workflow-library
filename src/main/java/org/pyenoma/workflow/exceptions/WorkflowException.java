package org.pyenoma.workflow.exceptions;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class WorkflowException extends Exception {
    private final String workflowId;

    public WorkflowException(String workflowId, String message) {
        super(message);
        this.workflowId = workflowId;
    }

    public WorkflowException(String workflowId, String message, Exception e) {
        super(message, e);
        this.workflowId = workflowId;
    }
}
