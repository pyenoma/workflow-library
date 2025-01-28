package org.pyenoma.workflow.context;

import org.pyenoma.workflow.exceptions.WorkflowException;
import org.springframework.stereotype.Component;

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
