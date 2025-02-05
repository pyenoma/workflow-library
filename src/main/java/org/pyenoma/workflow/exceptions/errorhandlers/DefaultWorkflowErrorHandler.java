package org.pyenoma.workflow.exceptions.errorhandlers;

import lombok.extern.log4j.Log4j2;
import org.pyenoma.workflow.IWorkflowTask;
import org.pyenoma.workflow.WorkflowNodeResult;
import org.pyenoma.workflow.context.IWorkflowContext;
import org.pyenoma.workflow.exceptions.WorkflowException;

@Log4j2
public class DefaultWorkflowErrorHandler implements IWorkflowErrorHandler {
    @Override
    public void handle(WorkflowException e, IWorkflowContext context, Class<? extends IWorkflowTask<?>> taskClass) {
        context.addExecution(taskClass, WorkflowNodeResult.FAILURE);
        log.error("An exception occurred during workflow execution, Exception: {}, context: {}", e, context);
    }
}
