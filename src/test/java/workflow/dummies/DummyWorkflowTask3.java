package workflow.dummies;

import workflow.IWorkflowTask;
import workflow.WorkflowNodeResult;
import workflow.context.IWorkflowContext;

public class DummyWorkflowTask3 implements IWorkflowTask {

    @Override
    public WorkflowNodeResult validate(IWorkflowContext context) {
        return null;
    }

    @Override
    public WorkflowNodeResult execute(IWorkflowContext context) {
        return null;
    }
}