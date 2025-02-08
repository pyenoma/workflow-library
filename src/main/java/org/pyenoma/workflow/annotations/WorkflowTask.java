package org.pyenoma.workflow.annotations;

import org.pyenoma.workflow.IWorkflowTask;
import org.pyenoma.workflow.context.IWorkflowContext;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface WorkflowTask {
    Class<? extends IWorkflowTask<? extends IWorkflowContext>> taskClass();

    Class<? extends IWorkflowTask<? extends IWorkflowContext>>[] next() default {};

}
