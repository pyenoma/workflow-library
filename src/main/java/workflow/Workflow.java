package workflow;

import lombok.NonNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record Workflow(String id, Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency) {

    public Workflow(@NonNull String id,
            @NonNull Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency) {
        this.id = id;
        this.adjacency = Collections.unmodifiableMap(adjacency);
    }

    @Override
    public String toString() {
        return "Workflow{" + "id='" + id + '\'' + ", adjacency=" + getAdjacencyString() + '}';
    }

    private String getAdjacencyString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> entry : this.adjacency()
                .entrySet()) {
            Class<? extends IWorkflowTask> task = entry.getKey();
            Set<Class<? extends IWorkflowTask>> nextTasks = entry.getValue();

            builder.append(task.getSimpleName()).append(" -> [");
            builder.append(nextTasks.stream().map(Class::getSimpleName).collect(Collectors.joining(", ")));
            builder.append("]\n");
        }
        return builder.toString();
    }
}
