package workflow.context;

import workflow.exceptions.WorkflowException;

import java.util.List;
import java.util.Map;

public interface IWorkflowContext {
    /**
     * Stores a value in the context.
     *
     * @param key   the key under which the value is stored
     * @param value the value to store
     */
    void put(String key, Object value);

    /**
     * Retrieves a value from the context.
     *
     * @param key  the key under which the value is stored
     * @param type the expected type of the value
     * @param <T>  the type parameter
     * @return the value associated with the key, or null if not found
     */
    <T> T get(String key, Class<T> type);

    /**
     * Returns an unmodifiable view of the context data.
     *
     * @return an unmodifiable map of the context data
     */
    Map<String, Object> getAll();

    /**
     * Adds an exception to the context.
     *
     * @param exception the exception to add
     */
    void addException(WorkflowException exception);

    /**
     * Retrieves the exceptions stored in the context, if any.
     *
     * @return a list of exceptions, or an empty list if none are stored
     */
    List<WorkflowException> getExceptions();
}
