package org.pyenoma.workflow.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.pyenoma.workflow.IWorkflowTask;
import org.pyenoma.workflow.WorkflowNodeResult;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@ToString
public class AbstractWorkflowContext implements IWorkflowContext {
    private final String workflowId;

    private final Map<Class<? extends IWorkflowTask<?>>, WorkflowNodeResult> executions = Collections.synchronizedMap(
            new LinkedHashMap<>());

    public void addExecution(Class<? extends IWorkflowTask<?>> task, WorkflowNodeResult result) {
        executions.put(task, result);
    }
}
