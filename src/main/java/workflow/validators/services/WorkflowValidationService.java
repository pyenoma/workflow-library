package workflow.validators.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import workflow.Workflow;
import workflow.validators.workflowvalidators.IWorkflowValidator;

import java.util.List;

@RequiredArgsConstructor
@Component
public class WorkflowValidationService {
    private final List<IWorkflowValidator> validators;

    public void validate(Workflow workflow) {
        validators.forEach(validator -> validator.validate(workflow));
    }
}
