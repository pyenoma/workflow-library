package workflow.exceptions.errorhandlers;

import workflow.IWorkflowTask;
import workflow.context.IWorkflowContext;
import workflow.exceptions.WorkflowException;

public interface IWorkflowErrorHandler {

    /**
     * Handles an error that occurs during workflow execution.
     *
     * @param e       the exception that was thrown
     * @param context the workflow context
     */
    void handle(WorkflowException e, IWorkflowContext context, Class<? extends IWorkflowTask> taskClass);

}
