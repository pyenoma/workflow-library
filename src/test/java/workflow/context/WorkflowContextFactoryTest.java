package workflow.context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WorkflowContextFactoryTest {

    @Test
    void testCreateContext() {
        WorkflowContextFactory factory = new WorkflowContextFactory();
        DefaultWorkflowContext context = factory.createContext(DefaultWorkflowContext.class, "WorkflowId");
        assertNotNull(context);
    }
}