package workflow.dummies;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.pyenoma.workflow.IWorkflowTask;
import org.pyenoma.workflow.WorkflowNodeResult;
import org.pyenoma.workflow.annotations.WorkflowTaskBean;
import org.pyenoma.workflow.context.IWorkflowContext;
import org.pyenoma.workflow.exceptions.WorkflowException;

@Log4j2
@Getter
@Setter
@WorkflowTaskBean
public class DummyWorkflowTaskThatThrowsException implements IWorkflowTask<IWorkflowContext> {

    @Override
    public WorkflowNodeResult execute(IWorkflowContext context) {
        log.info("Executing DummyWorkflowTaskThatThrowsException");
        throw new WorkflowException("DummyWorkflowTaskThatThrowsException threw an exception");
    }
}
