package org.pyenoma.workflow.execution;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.context.IWorkflowContext;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Log4j2
@RequiredArgsConstructor
public class WorkflowTasksProcessorFactory {

    private final ThreadPoolTaskExecutor taskExecutor;

    private final ApplicationContext applicationContext;

    public <T extends IWorkflowContext> WorkflowTasksProcessor<T> create(Workflow<T> workflow, T context)
            throws InterruptedException {
        return new WorkflowTasksProcessor<>(workflow, context, taskExecutor, applicationContext);
    }

}
