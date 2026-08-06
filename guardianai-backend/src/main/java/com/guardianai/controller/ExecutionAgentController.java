package com.guardianai.controller;

import com.guardianai.dto.CreateTaskRequest;
import com.guardianai.dto.TaskDto;
import com.guardianai.service.ExecutionAgentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agents/execution")
public class ExecutionAgentController {

    private final ExecutionAgentService executionAgentService;

    public ExecutionAgentController(ExecutionAgentService executionAgentService) {
        this.executionAgentService = executionAgentService;
    }

    @PostMapping
    public ResponseEntity<TaskDto> executeAgentTask(@Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok(executionAgentService.createTask(request));
    }
}
