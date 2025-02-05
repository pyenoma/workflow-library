package org.pyenoma.workflow.context;

@FunctionalInterface
public interface IWorkflowContextFactory<T extends IWorkflowContext> {
    T create();
}
