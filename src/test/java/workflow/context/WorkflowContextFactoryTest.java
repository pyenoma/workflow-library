package workflow.context;

import org.junit.jupiter.api.Test;
import org.pyenoma.workflow.context.DefaultWorkflowContext;
import org.pyenoma.workflow.context.WorkflowContextFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WorkflowContextFactoryTest {

    @Test
    void testCreate() {
        WorkflowContextFactory factory = new WorkflowContextFactory();
        DefaultWorkflowContext context = factory.create(DefaultWorkflowContext.class, "WorkflowId");
        assertNotNull(context);
    }
}