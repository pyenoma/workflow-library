package workflow.dummies;

import workflow.IWorkflowTask;
import workflow.WorkflowNodeResult;
import workflow.annotations.WorkflowTaskBean;
import workflow.context.IWorkflowContext;

@WorkflowTaskBean
public class DummyWorkflowTask implements IWorkflowTask {

    @Override
    public WorkflowNodeResult validate(IWorkflowContext context) {
        return null;
    }

    @Override
    public WorkflowNodeResult execute(IWorkflowContext context) {
        return null;
    }
}