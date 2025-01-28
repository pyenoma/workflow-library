package org.pyenoma.workflow;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.pyenoma.workflow.annotations.WorkflowDefinition;
import org.pyenoma.workflow.annotations.WorkflowTask;
import org.pyenoma.workflow.validators.services.WorkflowDefinitionValidationService;
import org.pyenoma.workflow.validators.services.WorkflowValidationService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
            workflowDefinitionValidationService.validate(workflowId, workflowDefinitionBeanType);
            Workflow workflow = build(workflowId,
                    Arrays.stream(workflowDefinitionBeanType.getAnnotation(WorkflowDefinition.class).tasks()).toList());
            workflowValidationService.validate(workflow);
            workflowRegistry.register(workflowId, workflow);
        });
    }

    private Workflow build(String workflowId, List<WorkflowTask> workflowTasks) {
        Map<Class<? extends IWorkflowTask>, Set<Class<? extends IWorkflowTask>>> adjacency = new HashMap<>();
        workflowTasks.forEach(workflowTask -> adjacency.put(workflowTask.taskClass(),
                Arrays.stream(workflowTask.next()).collect(Collectors.toSet())));
        return new Workflow(workflowId, adjacency);
    }
}

