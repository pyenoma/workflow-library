package workflow.dummies;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.pyenoma.workflow.AbstractWorkflowTask;
import org.pyenoma.workflow.WorkflowNodeResult;
import org.pyenoma.workflow.annotations.WorkflowTaskBean;
import org.pyenoma.workflow.context.IWorkflowContext;

@Log4j2
@Getter
@Setter
@WorkflowTaskBean
public class DummyWorkflowTask2 extends AbstractWorkflowTask<IWorkflowContext> {
    @Override
    public WorkflowNodeResult execute() {
        log.info("Executing DummyWorkflowTask2");
        return WorkflowNodeResult.SUCCESS;
    }
}