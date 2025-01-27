package workflow.dummies;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import workflow.IWorkflowTask;
import workflow.WorkflowNodeResult;
import workflow.annotations.WorkflowTaskBean;
import workflow.context.IWorkflowContext;
import workflow.exceptions.WorkflowException;

@Log4j2
@Getter
@Setter
@WorkflowTaskBean
public class DummyWorkflowTaskThatThrowsException implements IWorkflowTask {

    @Override
    public WorkflowNodeResult execute(IWorkflowContext context) {
        log.info("Executing DummyWorkflowTaskThatThrowsException");
        throw new WorkflowException("DummyWorkflowTaskThatThrowsException threw an exception");
    }
}
