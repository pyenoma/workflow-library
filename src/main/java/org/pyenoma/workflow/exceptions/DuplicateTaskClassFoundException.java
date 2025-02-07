package org.pyenoma.workflow.exceptions;

import org.pyenoma.workflow.annotations.WorkflowDefinition;

public class DuplicateTaskClassFoundException extends WorkflowException {
    public DuplicateTaskClassFoundException(Class<?> workflowDefinitionClass, Class<?> taskClass) {
        super(workflowDefinitionClass.getAnnotation(WorkflowDefinition.class).id(),
                buildMessage(workflowDefinitionClass, taskClass));
    }

    private static String buildMessage(Class<?> workflowDefinitionClass, Class<?> taskClass) {
        return "Duplicate task class definition found | " + "Task class: " + taskClass.getName() + " "
                + "Workflow class: " + workflowDefinitionClass.getName();
    }
}
