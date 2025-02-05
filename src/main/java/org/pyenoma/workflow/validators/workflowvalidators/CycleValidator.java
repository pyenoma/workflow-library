package org.pyenoma.workflow.validators.workflowvalidators;

import org.pyenoma.workflow.IWorkflowTask;
import org.pyenoma.workflow.Workflow;
import org.pyenoma.workflow.context.IWorkflowContext;
import org.pyenoma.workflow.exceptions.CyclicWorkflowDefinedException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@Component
public class CycleValidator implements IWorkflowValidator {
    @Override
    public void validate(Workflow<? extends IWorkflowContext> workflow) {
        if (hasCycle(workflow.adjacency())) {
            throw new CyclicWorkflowDefinedException(workflow);
        }
    }

    private <T extends IWorkflowContext> boolean hasCycle(
            Map<Class<? extends IWorkflowTask<T>>, Set<Class<? extends IWorkflowTask<T>>>> adjacencyList) {
        /*
         * Kahn's algorithm for cycle detection in directed graphs
         */

        // Step 1: Calculate in-degree of each node
        Map<Class<? extends IWorkflowTask<?>>, Integer> inDegree = new HashMap<>();
        for (Map.Entry<Class<? extends IWorkflowTask<T>>, Set<Class<? extends IWorkflowTask<T>>>> entry : adjacencyList.entrySet()) {
            inDegree.putIfAbsent(entry.getKey(), 0);
            for (Class<? extends IWorkflowTask<?>> neighbor : entry.getValue()) {
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        // Step 2: Add nodes with in-degree 0 to a queue
        Queue<Class<? extends IWorkflowTask<?>>> queue = new LinkedList<>();
        for (Map.Entry<Class<? extends IWorkflowTask<?>>, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        // Step 3: Process the queue
        int processedNodes = 0;
        while (!queue.isEmpty()) {
            Class<? extends IWorkflowTask<?>> current = queue.poll();
            processedNodes++;

            // Reduce in-degree for neighbors
            for (Class<? extends IWorkflowTask<?>> neighbor : adjacencyList.getOrDefault(current, Set.of())) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Step 4: Check if all nodes were processed
        return processedNodes != inDegree.size();
    }
}
