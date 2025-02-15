package org.pyenoma.workflow.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "workflow.executor")
@Validated
@Getter
@Setter
public class WorkflowExecutorProperties {
    /**
     * The core number of threads to keep in the pool, even if they are idle.
     */
    @Min(value = 1, message = "Core pool size must be at least 1") private int corePoolSize = 10;

    /**
     * The maximum number of threads to allow in the pool.
     */
    @Min(value = 1, message = "Maximum pool size must be at least 1") private int maxPoolSize = 20;

    /**
     * The capacity of the queue used for holding tasks before they are executed.
     */
    @Min(value = 1, message = "Queue capacity must be at least 1") private int queueCapacity = 50;

    /**
     * The prefix to use for naming the threads created by this thread pool.
     */
    @NotBlank(message = "Thread name prefix cannot be blank") private String threadNamePrefix = "workflow-";
}
