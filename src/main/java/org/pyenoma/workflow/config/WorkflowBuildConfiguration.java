package org.pyenoma.workflow.config;

import org.pyenoma.workflow.WorkflowBuilder;
import org.pyenoma.workflow.WorkflowRegistry;
import org.pyenoma.workflow.validators.services.WorkflowDefinitionValidationService;
import org.pyenoma.workflow.validators.services.WorkflowValidationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkflowBuildConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WorkflowBuilder workflowBuilder(ApplicationContext applicationContext,
            WorkflowDefinitionValidationService workflowDefinitionValidationService,
            WorkflowValidationService workflowValidationService, WorkflowRegistry workflowRegistry) {
        return new WorkflowBuilder(applicationContext, workflowDefinitionValidationService, workflowValidationService,
                workflowRegistry);
    }
}