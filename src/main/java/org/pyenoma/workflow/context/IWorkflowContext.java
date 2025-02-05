package org.pyenoma.workflow.context;

import org.pyenoma.workflow.IWorkflowTask;
import org.pyenoma.workflow.WorkflowNodeResult;

import java.util.Map;

public interface IWorkflowContext {
    /**
     * Retrieves the ID of the workflow.
     *
     * @return the workflow ID
     */
    String getWorkflowId();

    /**
     * Retrieves the executed tasks of the workflow.
     * If the implementation is an ordered data structure, the result of this method will be ordered by the execution.
     *
     * @return the execution order
     */
    Map<Class<? extends IWorkflowTask<?>>, WorkflowNodeResult> getExecutions();

    /**
     * Adds a task to the execution order. This method is called implicitly by the framework when a task is executed.
     * If the implementation is an ordered data structure, the order will be preserved.
     *
     * @param task the task to add
     */
    void addExecution(Class<? extends IWorkflowTask<?>> task, WorkflowNodeResult result);
}
