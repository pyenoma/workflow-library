package workflow.context;

import org.junit.jupiter.api.Test;
import org.pyenoma.workflow.context.DefaultWorkflowContext;
import org.pyenoma.workflow.exceptions.WorkflowException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultWorkflowContextTest {
    private static final String WORKFLOW_ID = "WorkflowId";

    @Test
    void testGetPutAndGetAll() {
        DefaultWorkflowContext defaultWorkflowContext = new DefaultWorkflowContext(WORKFLOW_ID);
        String key = "key";
        String value = "value";
        defaultWorkflowContext.put(key, value);
        assertEquals(value, defaultWorkflowContext.get(key, String.class));
        assertEquals(1, defaultWorkflowContext.getAll().size());
    }

    @Test
    void testAddGetException() {
        DefaultWorkflowContext defaultWorkflowContext = new DefaultWorkflowContext(WORKFLOW_ID);
        WorkflowException testWorkflowException = new WorkflowException(WORKFLOW_ID, "Test Workflow Exception");
        defaultWorkflowContext.addException(testWorkflowException);
        assertEquals(1, defaultWorkflowContext.getExceptions().size());
        assertTrue(defaultWorkflowContext.getExceptions().contains(testWorkflowException));
    }

}