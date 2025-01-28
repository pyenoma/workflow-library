package workflow.dummies;

import org.pyenoma.workflow.annotations.WorkflowDefinition;
import org.pyenoma.workflow.annotations.WorkflowTask;

@WorkflowDefinition(id = "DummyWorkflowDefinitionBeanWithId", tasks = {
        @WorkflowTask(taskClass = DummyWorkflowTask.class, next = DummyWorkflowTask3.class),
        @WorkflowTask(taskClass = DummyWorkflowTask2.class, next = DummyWorkflowTask3.class)})
public class DummyWorkflowDefinitionBeanWithId {
}
