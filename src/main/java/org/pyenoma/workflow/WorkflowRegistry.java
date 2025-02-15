package org.pyenoma.workflow;

import org.pyenoma.workflow.context.IWorkflowContext;

public interface WorkflowRegistry {
    void register(String workflowId, Workflow<? extends IWorkflowContext> workflow);

    Workflow<? extends IWorkflowContext> getWorkflow(String workflowId);

}
