package workflow.exceptions.errorhandlers;

import lombok.extern.log4j.Log4j2;
import workflow.IWorkflowTask;
import workflow.WorkflowNodeResult;
import workflow.context.IWorkflowContext;
import workflow.exceptions.WorkflowException;

@Log4j2
public class DefaultWorkflowErrorHandler implements IWorkflowErrorHandler {
    @Override
    public void handle(WorkflowException e, IWorkflowContext context, Class<? extends IWorkflowTask> taskClass) {
        context.addExecution(taskClass, WorkflowNodeResult.FAILURE);
        log.error("An exception occurred during workflow execution, Exception: {}, context: {}", e, context);
        context.addException(e);
    }
}
