package workflow.validators.workflowdefinitionvalidators;

import workflow.exceptions.WorkflowException;

public interface IWorkflowDefinitionValidator {
    void validate(String workflowId, Class<?> workflowDefinition) throws WorkflowException;
}
