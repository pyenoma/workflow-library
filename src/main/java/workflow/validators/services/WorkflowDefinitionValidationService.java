package workflow.validators.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import workflow.validators.workflowdefinitionvalidators.IWorkflowDefinitionValidator;

import java.util.List;

@RequiredArgsConstructor
@Component
public class WorkflowDefinitionValidationService {
    private final List<IWorkflowDefinitionValidator> validators;

    public void validate(String workflowId, Class<?> workflowDefinitionType) {
        validators.forEach(validator -> validator.validate(workflowId, workflowDefinitionType));
    }
}
