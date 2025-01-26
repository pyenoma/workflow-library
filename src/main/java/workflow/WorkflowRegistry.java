package workflow;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WorkflowRegistry {
    private final Map<String, Workflow> registry = new HashMap<>();

    public void register(String workflowId, Workflow workflow) {
        registry.put(workflowId, workflow);
    }

    public Workflow getWorkflow(String workflowId) {
        return registry.get(workflowId);
    }

}
