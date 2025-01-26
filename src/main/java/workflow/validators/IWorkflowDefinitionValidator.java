package workflow.validators;

public interface IWorkflowDefinitionValidator {
    void validate(String workflowId, Class<?> workflowDefinition);
}
