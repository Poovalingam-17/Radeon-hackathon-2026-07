package com.guardianai.agent;

import com.guardianai.model.Task;
import com.guardianai.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;

@Component
@Slf4j
public class ExecutionAgent {

    private final Executor taskExecutor;
    private final TaskRepository taskRepository;
    private final RetryMechanism retryMechanism;

    public ExecutionAgent(@Qualifier("agentTaskExecutor") Executor taskExecutor,
                          TaskRepository taskRepository, RetryMechanism retryMechanism) {
        this.taskExecutor = taskExecutor;
        this.taskRepository = taskRepository;
        this.retryMechanism = retryMechanism;
    }

    public void execute(Task task, Runnable runnableTask) {
        log.info("Execution Agent scheduling task ID: {} for async processing...", task.getId());
        task.setStatus("RUNNING");
        task.setStartedAt(LocalDateTime.now());
        taskRepository.save(task);

        taskExecutor.execute(() -> {
            try {
                retryMechanism.executeWithRetry(() -> {
                    runnableTask.run();
                    return null;
                }, 3, 500);

                task.setStatus("COMPLETED");
                task.setCompletedAt(LocalDateTime.now());
                log.info("Task ID: {} executed successfully.", task.getId());
            } catch (Exception e) {
                log.error("Task ID: {} failed after all retries: {}", task.getId(), e.getMessage());
                task.setStatus("FAILED");
                task.setError(e.getMessage());
                task.setCompletedAt(LocalDateTime.now());
            } finally {
                taskRepository.save(task);
            }
        });
    }
}
