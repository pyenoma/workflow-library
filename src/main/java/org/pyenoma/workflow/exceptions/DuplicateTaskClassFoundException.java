package org.pyenoma.workflow.exceptions;

public class DuplicateTaskClassFoundException extends WorkflowException {
    public DuplicateTaskClassFoundException(Class<?> workflowDefinitionClass, Class<?> taskClass) {
        super(buildMessage(workflowDefinitionClass, taskClass));
    }

    private static String buildMessage(Class<?> workflowDefinitionClass, Class<?> taskClass) {
        return "Duplicate task class definition found | " + "Task class: " + taskClass.getName() + " "
                + "Workflow class: " + workflowDefinitionClass.getName();
    }
}
