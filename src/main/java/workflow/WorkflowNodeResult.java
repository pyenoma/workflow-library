package workflow;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import workflow.exceptions.WorkflowException;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkflowNodeResult {
    public enum Status {
        EXECUTE,
        SKIP,
        ERROR,
        STOP
    }
    private Status status;
    private List<String> nextNodes;
    private WorkflowException exception;

    public static WorkflowNodeResult execute(List<String> nextNodes) {
        return WorkflowNodeResult.builder().status(Status.EXECUTE).nextNodes(nextNodes != null ? nextNodes : List.of()).build();
    }

    public static WorkflowNodeResult skip() {
        return WorkflowNodeResult.builder().status(Status.SKIP).build();
    }

    public static WorkflowNodeResult error(WorkflowException exception) {
        return WorkflowNodeResult.builder().status(Status.ERROR).exception(exception).build();
    }

    public static WorkflowNodeResult stop() {
        return WorkflowNodeResult.builder().status(Status.STOP).build();
    }
}
