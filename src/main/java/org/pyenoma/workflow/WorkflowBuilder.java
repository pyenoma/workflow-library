package org.pyenoma.workflow;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.pyenoma.workflow.annotations.WorkflowDefinition;
import org.pyenoma.workflow.context.IWorkflowContext;
import org.pyenoma.workflow.exceptions.WorkflowException;
import org.pyenoma.workflow.validators.services.WorkflowDefinitionValidationService;
import org.pyenoma.workflow.validators.services.WorkflowValidationService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Component
public class WorkflowBuilder {
    private final ApplicationContext applicationContext;

    private final WorkflowDefinitionValidationService workflowDefinitionValidationService;

    private final WorkflowValidationService workflowValidationService;

    private final WorkflowRegistry workflowRegistry;

    private static String getWorkflowId(Class<?> workflowDefinitionBeanType) {
        String workflowId = workflowDefinitionBeanType.getAnnotation(WorkflowDefinition.class).id();
        if (StringUtils.isBlank(workflowId)) {
            workflowId = workflowDefinitionBeanType.getName();
        }
        return workflowId;
    }

    @PostConstruct
    public void build() {
        Set<Class<?>> workflowDefinitionBeanTypes = Arrays.stream(
                        applicationContext.getBeanNamesForAnnotation(WorkflowDefinition.class)).map(applicationContext::getType)
                .collect(Collectors.toSet());

        workflowDefinitionBeanTypes.forEach(workflowDefinitionBeanType -> {
            String workflowId = getWorkflowId(workflowDefinitionBeanType);

            try {
                workflowDefinitionValidationService.validate(workflowId, workflowDefinitionBeanType);
            } catch (WorkflowException e) {
                // We should not expect validation failures while building workflows, however, if they occur, we should
                // wrap them as unchecked exception and throw back to spring container so that the application is crashed.
                // We cannot allow the application to come up with invalid workflows.
                // This is done to fail fast and avoid potential issues when the workflows are actually executed.
                throw new RuntimeException(e);
            }

            Workflow<? extends IWorkflowContext> workflow = Workflow.builder().id(workflowId).adjacency(
                            Arrays.stream(workflowDefinitionBeanType.getAnnotation(WorkflowDefinition.class).tasks())
                                    .map(task -> Map.entry(task.taskClass(),
                                            Arrays.stream(task.next()).collect(Collectors.toSet())))
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))).build();

            try {
                workflowValidationService.validate(workflow);
            } catch (WorkflowException e) {
                // We should not expect validation failures while building workflows, however, if they occur, we should
                // wrap them as unchecked exception and throw back to spring container so that the application is crashed.
                // We cannot allow the application to come up with invalid workflows.
                // This is done to fail fast and avoid potential issues when the workflows are actually executed.
                throw new RuntimeException(e);
            }
            workflowRegistry.register(workflowId, workflow);
        });
    }
}

