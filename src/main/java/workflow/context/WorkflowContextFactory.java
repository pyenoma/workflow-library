package workflow.context;

import org.springframework.stereotype.Component;
import workflow.exceptions.WorkflowException;

@Component
public class WorkflowContextFactory {
    public <T extends IWorkflowContext> T createContext(Class<T> contextClass, String workflowId)
            throws WorkflowException {
        try {
            return contextClass.getConstructor(String.class).newInstance(workflowId);
        } catch (ReflectiveOperationException e) {
            throw new WorkflowException("Failed to create workflow context instance", e);
        }
    }
}
