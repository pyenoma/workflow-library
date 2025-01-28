package org.pyenoma.workflow.exceptions.errorhandlers;

import org.pyenoma.workflow.IWorkflowTask;
import org.pyenoma.workflow.context.IWorkflowContext;
import org.pyenoma.workflow.exceptions.WorkflowException;

public interface IWorkflowErrorHandler {

    /**
     * Handles an error that occurs during workflow execution.
     *
     * @param e       the exception that was thrown
     * @param context the workflow context
     */
    void handle(WorkflowException e, IWorkflowContext context, Class<? extends IWorkflowTask> taskClass);

}
