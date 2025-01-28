package workflow.validators.workflowdefinitionvalidators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import workflow.exceptions.DuplicateWorkflowIdException;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class DuplicateWorkflowIdValidator implements IWorkflowDefinitionValidator {
    private final Set<String> seenWorkflowIds;

    @Override
    public void validate(String workflowId, Class<?> workflowDefinition) throws DuplicateWorkflowIdException {
        if (!seenWorkflowIds.add(workflowId)) {
            throw new DuplicateWorkflowIdException(workflowId);
        }
    }
}
