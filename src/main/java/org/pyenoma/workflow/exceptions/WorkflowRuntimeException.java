package org.pyenoma.workflow.exceptions;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class WorkflowRuntimeException extends RuntimeException {
    private final String workflowId;

    public WorkflowRuntimeException(String workflowId, Exception e) {
        super(e);
        this.workflowId = workflowId;
    }
}
