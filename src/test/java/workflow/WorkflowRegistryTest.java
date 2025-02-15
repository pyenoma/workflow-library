package workflow;

import org.junit.jupiter.api.Test;
import org.pyenoma.workflow.DefaultWorkflowRegistry;
import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.WorkflowRegistry;
import org.pyenoma.workflow.context.IWorkflowContext;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkflowRegistryTest {

    private static final String WORKFLOW_ID = "WorkflowId";

    @Test
    void testRegisterAndGetWorkflow() {
        WorkflowRegistry registry = new DefaultWorkflowRegistry();
        Workflow<? extends IWorkflowContext> workflow = new Workflow<>(WORKFLOW_ID, new HashMap<>());
        registry.register(WORKFLOW_ID, workflow);
        assertEquals(workflow, registry.getWorkflow(WORKFLOW_ID));
    }

}