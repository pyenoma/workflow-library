package org.pyenoma.workflow;

import lombok.Getter;
import lombok.Setter;
import org.pyenoma.workflow.context.IWorkflowContext;

@Getter
@Setter
public abstract class AbstractWorkflowTask<T extends IWorkflowContext> implements IWorkflowTask<T> {
    private T context;
}
