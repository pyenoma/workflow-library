package org.pyenoma.workflow.execution;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.WorkflowRegistry;
import org.pyenoma.workflow.context.IWorkflowContext;
import org.pyenoma.workflow.context.IWorkflowContextFactory;
import org.pyenoma.workflow.exceptions.WorkflowException;

@Log4j2
@RequiredArgsConstructor
public class WorkflowExecutor {

    private final WorkflowRegistry workflowRegistry;

    private final WorkflowTasksProcessorFactory workflowTasksProcessorFactory;

    public <T extends IWorkflowContext> IWorkflowContext execute(@NonNull String workflowId,
            IWorkflowContextFactory<T> contextFactory)
            throws InterruptedException, WorkflowException {
        @SuppressWarnings("unchecked") Workflow<T> workflow = (Workflow<T>) workflowRegistry.getWorkflow(workflowId);
        if (workflow == null) {
            log.error("No workflow found with ID: {}", workflowId);
            return null;
        }
        log.info("Starting execution of workflow with ID: {}", workflowId);
        T context = contextFactory.create();
        workflowTasksProcessorFactory.create(workflow, context).process();
        log.info("Workflow {} execution completed.", workflowId);
        return context;
    }
}
