package org.pyenoma.workflow.exceptions.errorhandlers;

import org.pyenoma.workflow.IWorkflowTask;
import org.pyenoma.workflow.context.IWorkflowContext;

public interface IWorkflowErrorHandler<T extends IWorkflowContext, E extends Exception> {

    /**
     * Handles an error that occurs during workflow execution.
     *
     * @param e       the exception that was thrown
     * @param context the workflow context
     */
    void handle(E e, T context, Class<? extends IWorkflowTask<?>> taskClass);

}
