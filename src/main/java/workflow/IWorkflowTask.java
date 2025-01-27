package workflow;

import workflow.context.IWorkflowContext;
import workflow.exceptions.WorkflowException;

public interface IWorkflowTask {
    /**
     * Executes the node's logic.
     *
     * @param context the workflow context
     * @return a NodeResult indicating the next action
     */
    WorkflowNodeResult execute(IWorkflowContext context) throws WorkflowException;
}
