package workflow.dummies;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.pyenoma.workflow.IWorkflowTask;
import org.pyenoma.workflow.WorkflowNodeResult;
import org.pyenoma.workflow.annotations.WorkflowTaskBean;
import org.pyenoma.workflow.context.IWorkflowContext;

@Log4j2
@Getter
@Setter
@WorkflowTaskBean
public class DummyFailingWorkflowTask implements IWorkflowTask<IWorkflowContext> {
    @Override
    public WorkflowNodeResult execute(IWorkflowContext context) {
        log.info("Executing DummyFailingWorkflowTask");
        return WorkflowNodeResult.FAILURE;
    }
}