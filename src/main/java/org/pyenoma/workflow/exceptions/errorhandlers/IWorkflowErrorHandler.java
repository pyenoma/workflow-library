package org.pyenoma.workflow.exceptions.errorhandlers;

import org.pyenoma.workflow.IWorkflowTask;
import org.pyenoma.workflow.context.IWorkflowContext;

public interface IWorkflowErrorHandler {

    /**
     * Handles an error that occurs during workflow execution.
     *
     * @param e       the exception that was thrown
     * @param context the workflow context
     */
    void handle(Exception e, IWorkflowContext context, Class<? extends IWorkflowTask<?>> taskClass);

}
