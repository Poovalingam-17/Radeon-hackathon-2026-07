package com.guardianai.service;

import com.guardianai.agent.ExecutionAgent;
import com.guardianai.dto.CreateTaskRequest;
import com.guardianai.dto.TaskDto;
import com.guardianai.model.Agent;
import com.guardianai.model.Task;
import com.guardianai.model.User;
import com.guardianai.model.UserPrincipal;
import com.guardianai.repository.AgentRepository;
import com.guardianai.repository.TaskRepository;
import com.guardianai.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExecutionAgentService {

    private final TaskRepository taskRepository;
    private final AgentRepository agentRepository;
    private final UserRepository userRepository;
    private final ExecutionAgent executionAgent;

    public java.util.List<TaskDto> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId).stream().map(this::mapToTaskDto).collect(Collectors.toList());
    }

    public ExecutionAgentService(TaskRepository taskRepository, AgentRepository agentRepository,
                                 UserRepository userRepository, ExecutionAgent executionAgent) {
        this.taskRepository = taskRepository;
        this.agentRepository = agentRepository;
        this.userRepository = userRepository;
        this.executionAgent = executionAgent;
    }

    @Transactional(readOnly = true)
    public List<TaskDto> findAll() {
        log.info("Fetching all agent tasks");
        return taskRepository.findAll().stream().map(this::mapToTaskDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskDto findById(Long id) {
        log.info("Fetching task details for ID: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
        return mapToTaskDto(task);
    }

    @Transactional
    public TaskDto createTask(CreateTaskRequest request) {
        log.info("Creating execution task: {}", request.getName());
        Agent agent = agentRepository.findById(request.getAgentId())
                .orElseThrow(() -> new IllegalArgumentException("Agent not found with ID: " + request.getAgentId()));

        User user = getCurrentUserEntity();

        Task task = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .status("PENDING")
                .priority(request.getPriority() != null ? Integer.parseInt(request.getPriority()) : 1)
                .input(request.getInput())
                .agent(agent)
                .user(user)
                .build();

        Task savedTask = taskRepository.save(task);

        executionAgent.execute(savedTask, () -> {
            try {
                log.info("Starting processing task execution for payload: {}", savedTask.getInput());
                Thread.sleep(2000);
                savedTask.setOutput("Task execution completed successfully. Resources processed.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Task execution interrupted: " + e.getMessage(), e);
            }
        });

        return mapToTaskDto(savedTask);
    }

    @Transactional
    public TaskDto cancelTask(Long id) {
        log.info("Requesting cancellation for task ID: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));

        if ("PENDING".equals(task.getStatus())) {
            task.setStatus("CANCELLED");
            task = taskRepository.save(task);
        } else {
            throw new IllegalStateException("Only tasks in PENDING state can be cancelled. Current state: " + task.getStatus());
        }

        return mapToTaskDto(task);
    }

    private User getCurrentUserEntity() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            return userRepository.findById(userPrincipal.getId()).orElse(null);
        }
        return null;
    }

    private TaskDto mapToTaskDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .type(task.getType())
                .status(task.getStatus())
                .priority(String.valueOf(task.getPriority()))
                .agentId(task.getAgent() != null ? task.getAgent().getId() : null)
                .userId(task.getUser() != null ? task.getUser().getId() : null)
                .input(task.getInput())
                .output(task.getOutput())
                .error(task.getError())
                .startedAt(task.getStartedAt())
                .completedAt(task.getCompletedAt())
                .build();
    }
}
