package workflow.context;

import org.junit.jupiter.api.Test;
import workflow.exceptions.WorkflowException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultWorkflowContextTest {

    @Test
    void testGetPutAndGetAll() {
        IWorkflowContext defaultWorkflowContext = new DefaultWorkflowContext();
        String key = "key";
        String value = "value";
        defaultWorkflowContext.put(key, value);
        assertEquals(value, defaultWorkflowContext.get(key, String.class));
        assertEquals(1, defaultWorkflowContext.getAll().size());
    }

    @Test
    void testAddGetException() {
        IWorkflowContext defaultWorkflowContext = new DefaultWorkflowContext();
        WorkflowException testWorkflowException = new WorkflowException("Test Workflow Exception");
        defaultWorkflowContext.addException(testWorkflowException);
        assertEquals(1, defaultWorkflowContext.getExceptions().size());
        assertTrue(defaultWorkflowContext.getExceptions().contains(testWorkflowException));
    }

}