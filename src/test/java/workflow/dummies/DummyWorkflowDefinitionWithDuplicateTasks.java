package workflow.dummies;

import org.pyenoma.workflow.annotations.WorkflowDefinition;
import org.pyenoma.workflow.annotations.WorkflowTask;

@WorkflowDefinition(tasks = {@WorkflowTask(taskClass = DummyWorkflowTask.class, next = DummyWorkflowTask3.class),
        @WorkflowTask(taskClass = DummyWorkflowTask.class, next = DummyWorkflowTask3.class)})
public class DummyWorkflowDefinitionWithDuplicateTasks {
}
