package workflow;

import lombok.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record Workflow(Map<String, WorkflowNodeDefinition> nodes, List<String> startNodes) {

    public Workflow(@NonNull Map<String, WorkflowNodeDefinition> nodes, @NonNull List<String> startNodes) {
        this.nodes = Collections.unmodifiableMap(nodes);
        this.startNodes = Collections.unmodifiableList(startNodes);
    }
}
