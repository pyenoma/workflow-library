package workflow;

import workflow.context.IWorkflowContext;

public interface IWorkflowNode {
    /**
     * Validates whether the node should be executed.
     *
     * @param context the workflow context
     * @return a NodeResult indicating the action to take
     */
    WorkflowNodeResult validate(IWorkflowContext context);

    /**
     * Executes the node's logic.
     *
     * @param context the workflow context
     * @return a NodeResult indicating the next action
     */
    WorkflowNodeResult execute(IWorkflowContext context);
}
