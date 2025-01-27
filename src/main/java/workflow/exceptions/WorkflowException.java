package workflow.exceptions;

public class WorkflowException extends RuntimeException {
    public WorkflowException(String message) {
        super(message);
    }

    public WorkflowException(Exception e) {
        super(e);
    }

    public WorkflowException(String message, Exception e) {
        super(message, e);
    }
}
