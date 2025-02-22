package org.pyenoma.workflow.config;

import org.pyenoma.workflow.DefaultWorkflowRegistry;
import org.pyenoma.workflow.WorkflowRegistry;
import org.pyenoma.workflow.exceptions.errorhandlers.DefaultWorkflowErrorHandler;
import org.pyenoma.workflow.exceptions.errorhandlers.IWorkflowErrorHandler;
import org.pyenoma.workflow.execution.WorkflowExecutor;
import org.pyenoma.workflow.execution.WorkflowTasksProcessorFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class WorkflowExecutionConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WorkflowRegistry workflowRegistry() {
        return new DefaultWorkflowRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public WorkflowTasksProcessorFactory workflowTasksProcessorFactory(ThreadPoolTaskExecutor taskExecutor,
            ApplicationContext applicationContext) {
        return new WorkflowTasksProcessorFactory(taskExecutor, applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public WorkflowExecutor workflowExecutor(WorkflowRegistry registry, WorkflowTasksProcessorFactory factory) {
        return new WorkflowExecutor(registry, factory);
    }

    @Bean
    @ConditionalOnMissingBean
    public ThreadPoolTaskExecutor taskExecutor(WorkflowExecutorProperties properties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean(IWorkflowErrorHandler.class)
    public DefaultWorkflowErrorHandler defaultWorkflowErrorHandler() {
        return new DefaultWorkflowErrorHandler();
    }
}