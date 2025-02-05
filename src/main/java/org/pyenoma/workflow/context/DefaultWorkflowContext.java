package org.pyenoma.workflow.context;

import lombok.Getter;
import lombok.ToString;
import org.pyenoma.workflow.IWorkflowTask;
import org.pyenoma.workflow.WorkflowNodeResult;
import org.pyenoma.workflow.exceptions.WorkflowException;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@ToString(callSuper = true)
public class DefaultWorkflowContext extends AbstractWorkflowContext {
    private final Map<String, Object> data = new ConcurrentHashMap<>();
    private final List<WorkflowException> exceptions = new CopyOnWriteArrayList<>();

    private final Map<Class<? extends IWorkflowTask<?>>, WorkflowNodeResult> executions = Collections.synchronizedMap(
            new LinkedHashMap<>());

    public DefaultWorkflowContext(String workflowId) {
        super(workflowId);
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        return type.cast(data.get(key));
    }

    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(data);
    }

    public void addException(WorkflowException exception) {
        exceptions.add(exception);
    }

    public List<WorkflowException> getExceptions() {
        return Collections.unmodifiableList(exceptions);
    }

    public void addExecution(Class<? extends IWorkflowTask<?>> task, WorkflowNodeResult result) {
        executions.put(task, result);
    }

}
