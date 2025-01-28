package workflow.validators.workflowdefinitionvalidators;

import org.springframework.stereotype.Component;
import workflow.annotations.WorkflowDefinition;
import workflow.annotations.WorkflowTask;
import workflow.exceptions.DuplicateTaskClassFoundException;

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
