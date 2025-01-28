package org.pyenoma.workflow.validators.workflowdefinitionvalidators;

import org.pyenoma.workflow.annotations.WorkflowDefinition;
import org.pyenoma.workflow.annotations.WorkflowTask;
import org.pyenoma.workflow.exceptions.DuplicateTaskClassFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DuplicateTaskClassesValidator implements IWorkflowDefinitionValidator {
    @Override
    public void validate(String workflowId, Class<?> workflowDefinitionType) throws DuplicateTaskClassFoundException {
        Set<Class<?>> seenClasses = new HashSet<>();
        WorkflowTask[] tasks = workflowDefinitionType.getAnnotation(WorkflowDefinition.class).tasks();
        for (WorkflowTask task : tasks) {
            if (!seenClasses.add(task.taskClass())) {
                throw new DuplicateTaskClassFoundException(workflowDefinitionType, task.taskClass());
            }
        }
    }
}
