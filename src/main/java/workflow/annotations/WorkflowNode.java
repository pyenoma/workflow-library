package workflow.annotations;

import workflow.exceptions.errorhandlers.DefaultWorkflowErrorHandler;
import workflow.exceptions.errorhandlers.IWorkflowErrorHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WorkflowNode {
    String id();
    Class<? extends IWorkflowErrorHandler> errorHandler() default DefaultWorkflowErrorHandler.class;
    boolean start() default false;
}
