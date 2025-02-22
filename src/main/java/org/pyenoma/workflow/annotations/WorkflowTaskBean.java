package org.pyenoma.workflow.annotations;

import org.pyenoma.workflow.context.IWorkflowContext;
import org.pyenoma.workflow.exceptions.errorhandlers.DefaultWorkflowErrorHandler;
import org.pyenoma.workflow.exceptions.errorhandlers.IWorkflowErrorHandler;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public @interface WorkflowTaskBean {
    Class<? extends IWorkflowErrorHandler<? extends IWorkflowContext, ? extends Exception>> errorHandler() default DefaultWorkflowErrorHandler.class;
}
