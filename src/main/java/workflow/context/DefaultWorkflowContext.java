package workflow.context;

import lombok.Getter;
import lombok.ToString;
import workflow.annotations.WorkflowContextBean;
import workflow.exceptions.WorkflowException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@ToString
@WorkflowContextBean
public class DefaultWorkflowContext implements IWorkflowContext{
    private final Map<String, Object> data = new ConcurrentHashMap<>();
    private final List<WorkflowException> exceptions = new CopyOnWriteArrayList<>();

    @Override
    public void put(String key, Object value) {
        data.put(key, value);
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        return type.cast(data.get(key));
    }

    @Override
    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(data);
    }

    @Override
    public void addException(WorkflowException exception) {
        exceptions.add(exception);
    }

    @Override
    public List<WorkflowException> getExceptions() {
        return Collections.unmodifiableList(exceptions);
    }
}
