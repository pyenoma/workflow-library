package org.pyenoma.workflow;

import org.pyenoma.workflow.context.IWorkflowContext;

import java.util.HashMap;
import java.util.Map;

public class DefaultWorkflowRegistry implements WorkflowRegistry {
    private final Map<String, Workflow<? extends IWorkflowContext>> registry = new HashMap<>();

    public void register(String workflowId, Workflow<? extends IWorkflowContext> workflow) {
        registry.put(workflowId, workflow);
    }

    public Workflow<? extends IWorkflowContext> getWorkflow(String workflowId) {
        return registry.get(workflowId);
    }
}
