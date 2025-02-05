package org.pyenoma.workflow.execution;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.context.DefaultWorkflowContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class WorkflowTasksProcessorFactoryTest {

    @InjectMocks private WorkflowTasksProcessorFactory factory;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() throws InterruptedException {
        assertInstanceOf(WorkflowTasksProcessor.class, factory.create(new Workflow<>("workflowId", Map.of()),
                        new DefaultWorkflowContext("workflowId")));
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }
}