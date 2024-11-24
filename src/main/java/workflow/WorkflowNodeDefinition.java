package workflow;

import workflow.exceptions.errorhandlers.IWorkflowErrorHandler;

public record WorkflowNodeDefinition(String id, IWorkflowNode nodeInstance, Class<? extends IWorkflowErrorHandler> errorHandler) {

}
