package workflow.validators;

import workflow.exceptions.WorkflowException;

public interface IWorkflowDefinitionValidator {
    void validate(String workflowId, Class<?> workflowDefinition) throws WorkflowException;
}
