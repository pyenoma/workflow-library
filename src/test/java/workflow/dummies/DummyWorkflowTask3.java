package workflow.dummies;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import workflow.IWorkflowTask;
import workflow.WorkflowNodeResult;
import workflow.annotations.WorkflowTaskBean;
import workflow.context.IWorkflowContext;

@Log4j2
@Getter
@Setter
@WorkflowTaskBean
public class DummyWorkflowTask3 implements IWorkflowTask {
    @Override
    public WorkflowNodeResult execute(IWorkflowContext context) {
        log.info("Executing DummyWorkflowTask3");
        return WorkflowNodeResult.SUCCESS;
    }
}