package org.pyenoma.workflow.validators.workflowdefinitionvalidators;

import lombok.RequiredArgsConstructor;
import org.pyenoma.workflow.exceptions.DuplicateWorkflowIdException;

import java.util.Set;

@RequiredArgsConstructor
public class DuplicateWorkflowIdValidator implements IWorkflowDefinitionValidator {
    private final Set<String> seenWorkflowIds;

    @Override
    public void validate(String workflowId, Class<?> workflowDefinition) throws DuplicateWorkflowIdException {
        if (!seenWorkflowIds.add(workflowId)) {
            throw new DuplicateWorkflowIdException(workflowId);
        }
    }
}
