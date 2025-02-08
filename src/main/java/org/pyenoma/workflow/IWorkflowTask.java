package org.pyenoma.workflow;

import org.pyenoma.workflow.context.IWorkflowContext;
import org.pyenoma.workflow.exceptions.WorkflowException;

public interface IWorkflowTask<T extends IWorkflowContext> {
    /**
     * Executes the node's logic.
     *
     * @return a NodeResult indicating the next action
     */
    WorkflowNodeResult execute() throws WorkflowException;

    /**
     * Returns the context of the task.
     *
     * @return the context of the task
     */
    T getContext();

    /**
     * Sets the context of the task.
     *
     * @param context the context of the task
     */
    void setContext(T context);
}
