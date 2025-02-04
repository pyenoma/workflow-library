package org.pyenoma.workflow.execution;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.WorkflowRegistry;
import org.pyenoma.workflow.context.IWorkflowContext;
import org.pyenoma.workflow.context.WorkflowContextFactory;
import org.pyenoma.workflow.exceptions.WorkflowException;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class WorkflowExecutor {

    private final WorkflowRegistry workflowRegistry;

    private final WorkflowTasksProcessorFactory workflowTasksProcessorFactory;
    private final WorkflowContextFactory workflowContextFactory;

    public IWorkflowContext execute(@NonNull String workflowId)
            throws InterruptedException, WorkflowException {
        Workflow workflow = workflowRegistry.getWorkflow(workflowId);
        if (workflow == null) {
            log.error("No workflow found with ID: {}", workflowId);
            return null;
        }
        log.info("Starting execution of workflow with ID: {}", workflowId);
        IWorkflowContext context = workflowContextFactory.create(workflow.context(), workflow.id());
        workflowTasksProcessorFactory.create(workflow, context).process();
        log.info("Workflow {} execution completed.", workflowId);
        return context;
    }
}
