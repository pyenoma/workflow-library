package workflow.context;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import workflow.exceptions.WorkflowException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@ToString
public class DefaultWorkflowContext implements IWorkflowContext{
    private final Map<String, Object> data = new ConcurrentHashMap<>();
    private final List<WorkflowException> exceptions = new CopyOnWriteArrayList<>();

    @Override
    public void put(String key, Object value) {
        data.put(key, value);
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        Object value = data.get(key);
        if (value != null) {
            return type.cast(value);
        } else {
            return null;
        }
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
