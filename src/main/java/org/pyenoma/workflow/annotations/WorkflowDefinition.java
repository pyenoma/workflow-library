package org.pyenoma.workflow.annotations;

import org.pyenoma.workflow.context.DefaultWorkflowContext;
import org.pyenoma.workflow.context.IWorkflowContext;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface WorkflowDefinition {
    String id() default "";
    WorkflowTask[] tasks();

    Class<? extends IWorkflowContext> context() default DefaultWorkflowContext.class;
}
