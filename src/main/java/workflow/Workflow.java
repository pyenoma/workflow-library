package workflow;

import lombok.NonNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public record Workflow(Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency) {

    public Workflow(@NonNull Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency) {
        this.adjacency = Collections.unmodifiableMap(adjacency);
    }
}
