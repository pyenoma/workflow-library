package workflow.exceptions.errorhandlers;

import lombok.extern.log4j.Log4j2;
import workflow.context.IWorkflowContext;
import workflow.exceptions.WorkflowException;

@Log4j2
public class DefaultWorkflowErrorHandler implements IWorkflowErrorHandler {
    @Override
    public void handle(WorkflowException e, IWorkflowContext context) {
        log.error("An error occurred during workflow execution", e);
        context.addException(e);
    }
}
