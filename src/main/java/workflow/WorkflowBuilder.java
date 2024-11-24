package workflow;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import workflow.annotations.WorkflowNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class WorkflowBuilder {
    private final ApplicationContext applicationContext;

    public Workflow build() {
        Map<String, WorkflowNodeDefinition> nodes = new HashMap<>();
        List<String> startNodes = new ArrayList<>();

        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(WorkflowNode.class);
        for (Object bean: beansWithAnnotation.values()) {
            WorkflowNode annotation = bean.getClass().getAnnotation(WorkflowNode.class);
            String id = annotation.id();
            if (nodes.containsKey(id)) {
                throw new IllegalArgumentException("Duplicate node id: " + id);
            }
            if (!(bean instanceof IWorkflowNode)) {
                throw new IllegalArgumentException("Node must implement IWorkflowNode: " + bean.getClass().getName());
            }
            WorkflowNodeDefinition nodeDefinition = new WorkflowNodeDefinition(id, (IWorkflowNode) bean, annotation.errorHandler());
            nodes.put(id, nodeDefinition);
            if (annotation.start()) {
                startNodes.add(id);
            }
        }
        if (startNodes.isEmpty()) {
            throw new IllegalArgumentException("No start node found");
        }
        return new Workflow(nodes, startNodes);
    }
}
