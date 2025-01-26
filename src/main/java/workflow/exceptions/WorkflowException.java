package workflow.exceptions;

public class WorkflowException extends RuntimeException implements IWorkflowException {
    public WorkflowException(String message) {
        super(message);
    }

    public WorkflowException(Exception e) {
        super(e);
    }
}
