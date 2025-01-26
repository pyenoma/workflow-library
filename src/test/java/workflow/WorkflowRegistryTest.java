package workflow;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkflowRegistryTest {

    @Test
    void testRegisterAndGetWorkflow() {
        WorkflowRegistry registry = new WorkflowRegistry();
        Workflow workflow = new Workflow(new HashMap<>());
        registry.register("WorkflowId", workflow);
        assertEquals(workflow, registry.getWorkflow("WorkflowId"));
    }

}