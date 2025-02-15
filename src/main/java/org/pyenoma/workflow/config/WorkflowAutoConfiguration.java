package org.pyenoma.workflow.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(WorkflowExecutorProperties.class)
@Import({WorkflowExecutionConfiguration.class, WorkflowValidationConfiguration.class, WorkflowBuildConfiguration.class})
public class WorkflowAutoConfiguration {
}