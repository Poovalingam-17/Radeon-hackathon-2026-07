package com.guardianai.controller;

import com.guardianai.agent.MemoryAgent;
import com.guardianai.service.MemoryAgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agents/memory")
public class MemoryAgentController {

    private final MemoryAgentService memoryAgentService;

    public MemoryAgentController(MemoryAgentService memoryAgentService) {
        this.memoryAgentService = memoryAgentService;
    }

    @PostMapping
    public ResponseEntity<MemoryAgent.MemoryContext> getContext(@RequestBody String prompt) {
        MemoryAgent.MemoryContext context = memoryAgentService.retrieveCurrentContext(prompt);
        return ResponseEntity.ok(context);
    }
}
