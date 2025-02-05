package org.pyenoma.workflow.autoconfig;

import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class WorkflowAutoConfigurationTest {

    private final WorkflowAutoConfiguration workflowAutoConfiguration = new WorkflowAutoConfiguration();

    @Test
    void taskExecutor() {
        assertInstanceOf(ThreadPoolTaskExecutor.class, workflowAutoConfiguration.taskExecutor());
    }
}