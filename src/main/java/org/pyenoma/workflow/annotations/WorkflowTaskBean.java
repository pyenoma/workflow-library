package org.pyenoma.workflow.annotations;

import org.pyenoma.workflow.exceptions.errorhandlers.DefaultWorkflowErrorHandler;
import org.pyenoma.workflow.exceptions.errorhandlers.IWorkflowErrorHandler;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
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
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public @interface WorkflowTaskBean {
    Class<? extends IWorkflowErrorHandler> errorHandler() default DefaultWorkflowErrorHandler.class;
}
