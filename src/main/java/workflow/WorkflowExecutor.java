package workflow;

import jakarta.inject.Provider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import workflow.context.IWorkflowContext;
import workflow.exceptions.WorkflowException;
import workflow.exceptions.errorhandlers.IWorkflowErrorHandler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class WorkflowExecutor {

    private final Provider<IWorkflowContext> workflowContextProvider;

    private final ApplicationContext applicationContext;

    public void execute(@NonNull Workflow workflow) {
        IWorkflowContext context = workflowContextProvider.get();
        Map<String, WorkflowNodeDefinition> nodes = workflow.nodes();
        Set<String> visitedNodes = new HashSet<>();
        Queue<String> nodesToVisit = new LinkedList<>(workflow.startNodes());
        while (!nodesToVisit.isEmpty()) {
            String nodeId = nodesToVisit.poll();
            if (visitedNodes.contains(nodeId)) {
                continue;
            }
            visitedNodes.add(nodeId);
            WorkflowNodeDefinition workflowNodeDefinition = nodes.get(nodeId);
            if (workflowNodeDefinition == null) {
                throw new IllegalArgumentException("Node not found: " + nodeId);
            }
            IWorkflowNode nodeInstance = workflowNodeDefinition.nodeInstance();
            try {
                // validation
                WorkflowNodeResult validationResult = nodeInstance.validate(context);
                if (handleNodeResult(validationResult, workflowNodeDefinition, context, nodesToVisit)) {
                    return;
                }
                // execution
                WorkflowNodeResult executionResult = nodeInstance.execute(context);
                if (handleNodeResult(executionResult, workflowNodeDefinition, context, nodesToVisit)) {
                    return;
                }
            } catch (WorkflowException e) {
                handleError(e, workflowNodeDefinition.errorHandler(), context);
            }
        }
    }

    private void handleError(WorkflowException e, Class<? extends IWorkflowErrorHandler> errorHandler, @NonNull IWorkflowContext context) {
        applicationContext.getBean(errorHandler).handle(e, context);
    }

    private boolean handleNodeResult(WorkflowNodeResult validationResult, WorkflowNodeDefinition workflowNodeDefinition, @NonNull IWorkflowContext context, Queue<String> nodesToVisit) {
        switch (validationResult.getStatus()) {
            case EXECUTE -> {
                if (validationResult.getNextNodes() != null && !validationResult.getNextNodes().isEmpty()) {
                    nodesToVisit.addAll(validationResult.getNextNodes());
                }
            }
            case SKIP -> {
                // do nothing
            }
            case ERROR -> {
                handleError(validationResult.getException(), workflowNodeDefinition.errorHandler(), context);
                return true;
            }
            case STOP -> {
                return true;
            }
            default -> throw new IllegalArgumentException("Unknown node result status: " + validationResult.getStatus());
        }
        return false;
    }
}
