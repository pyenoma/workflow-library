package workflow.dummies;

import workflow.annotations.WorkflowDefinition;
import workflow.annotations.WorkflowTask;

@WorkflowDefinition(tasks = {@WorkflowTask(taskClass = DummyWorkflowTask.class, next = DummyWorkflowTask3.class),
        @WorkflowTask(taskClass = DummyWorkflowTask2.class, next = DummyWorkflowTask3.class)})
public class DummyWorkflowDefinitionBeanWithNoId {
}
