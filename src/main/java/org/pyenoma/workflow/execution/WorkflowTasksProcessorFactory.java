package org.pyenoma.workflow.execution;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.context.IWorkflowContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Log4j2
@RequiredArgsConstructor
@Component
public class WorkflowTasksProcessorFactory {

    private final Executor taskExecutor;

    private final ApplicationContext applicationContext;

    public WorkflowTasksProcessor create(Workflow workflow, IWorkflowContext context) throws InterruptedException {
        return new WorkflowTasksProcessor(workflow, context, taskExecutor, applicationContext);
    }

}
