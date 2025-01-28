package org.pyenoma.workflow.validators.workflowdefinitionvalidators;

import org.pyenoma.workflow.exceptions.WorkflowException;

public interface IWorkflowDefinitionValidator {
    void validate(String workflowId, Class<?> workflowDefinition) throws WorkflowException;
}
