package org.pyenoma.workflow.config;

import org.pyenoma.workflow.validators.services.WorkflowDefinitionValidationService;
import org.pyenoma.workflow.validators.services.WorkflowValidationService;
import org.pyenoma.workflow.validators.workflowdefinitionvalidators.DuplicateTaskClassesValidator;
import org.pyenoma.workflow.validators.workflowdefinitionvalidators.DuplicateWorkflowIdValidator;
import org.pyenoma.workflow.validators.workflowdefinitionvalidators.IWorkflowDefinitionValidator;
import org.pyenoma.workflow.validators.workflowvalidators.CycleValidator;
import org.pyenoma.workflow.validators.workflowvalidators.IWorkflowValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;

@Configuration
public class WorkflowValidationConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DuplicateTaskClassesValidator duplicateTaskClassesValidator() {
        return new DuplicateTaskClassesValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public DuplicateWorkflowIdValidator duplicateWorkflowIdValidator() {
        return new DuplicateWorkflowIdValidator(new HashSet<>());
    }

    @Bean
    @ConditionalOnMissingBean
    public CycleValidator cycleValidator() {
        return new CycleValidator();
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
}