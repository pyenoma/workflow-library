package org.pyenoma.workflow.autoconfig;

import org.pyenoma.workflow.DefaultWorkflowRegistry;
import org.pyenoma.workflow.WorkflowBuilder;
import org.pyenoma.workflow.WorkflowRegistry;
import org.pyenoma.workflow.exceptions.errorhandlers.DefaultWorkflowErrorHandler;
import org.pyenoma.workflow.exceptions.errorhandlers.IWorkflowErrorHandler;
import org.pyenoma.workflow.execution.WorkflowExecutor;
import org.pyenoma.workflow.execution.WorkflowTasksProcessorFactory;
import org.pyenoma.workflow.validators.services.WorkflowDefinitionValidationService;
import org.pyenoma.workflow.validators.services.WorkflowValidationService;
import org.pyenoma.workflow.validators.workflowdefinitionvalidators.DuplicateTaskClassesValidator;
import org.pyenoma.workflow.validators.workflowdefinitionvalidators.DuplicateWorkflowIdValidator;
import org.pyenoma.workflow.validators.workflowdefinitionvalidators.IWorkflowDefinitionValidator;
import org.pyenoma.workflow.validators.workflowvalidators.CycleValidator;
import org.pyenoma.workflow.validators.workflowvalidators.IWorkflowValidator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@AutoConfiguration
@EnableConfigurationProperties(WorkflowExecutorProperties.class)
public class WorkflowAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WorkflowRegistry workflowRegistry() {
        return new DefaultWorkflowRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public WorkflowBuilder workflowBuilder(ApplicationContext applicationContext,
            WorkflowDefinitionValidationService workflowDefinitionValidationService,
            WorkflowValidationService workflowValidationService, WorkflowRegistry workflowRegistry) {
        return new WorkflowBuilder(applicationContext, workflowDefinitionValidationService, workflowValidationService,
                workflowRegistry);
    }

    @Bean
    @ConditionalOnMissingBean
    public WorkflowExecutor workflowExecutor(WorkflowRegistry registry, WorkflowTasksProcessorFactory factory) {
        return new WorkflowExecutor(registry, factory);
    }

    @Bean
    @ConditionalOnMissingBean
    public WorkflowTasksProcessorFactory workflowTasksProcessorFactory(ThreadPoolTaskExecutor taskExecutor,
            ApplicationContext applicationContext) {
        return new WorkflowTasksProcessorFactory(taskExecutor, applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public ThreadPoolTaskExecutor taskExecutor(WorkflowExecutorProperties properties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean(IWorkflowErrorHandler.class)
    public IWorkflowErrorHandler defaultWorkflowErrorHandler() {
        return new DefaultWorkflowErrorHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public WorkflowDefinitionValidationService workflowDefinitionValidationService(
            List<IWorkflowDefinitionValidator> validators) {
        return new WorkflowDefinitionValidationService(validators);
    }

    @Bean
    @ConditionalOnMissingBean
    public WorkflowValidationService workflowValidationService(List<IWorkflowValidator> validators) {
        return new WorkflowValidationService(validators);
    }

    @Bean
    @ConditionalOnMissingBean
    public DuplicateTaskClassesValidator duplicateTaskClassesValidator() {
        return new DuplicateTaskClassesValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public CycleValidator cycleValidator() {
        return new CycleValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public DuplicateWorkflowIdValidator duplicateWorkflowIdValidator() {
        return new DuplicateWorkflowIdValidator(new HashSet<>());
    }
}
