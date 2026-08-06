package com.guardianai.controller;

import com.guardianai.dto.CreateTaskRequest;
import com.guardianai.dto.TaskDto;
import com.guardianai.service.ExecutionAgentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final ExecutionAgentService executionAgentService;

    public TaskController(ExecutionAgentService executionAgentService) {
        this.executionAgentService = executionAgentService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EXECUTE_TASK')")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(executionAgentService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EXECUTE_TASK')")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(executionAgentService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('EXECUTE_TASK')")
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok(executionAgentService.createTask(request));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('EXECUTE_TASK')")
    public ResponseEntity<TaskDto> cancelTask(@PathVariable Long id) {
        return ResponseEntity.ok(executionAgentService.cancelTask(id));
    }
}
