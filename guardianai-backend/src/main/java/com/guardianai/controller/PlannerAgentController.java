package com.guardianai.controller;

import com.guardianai.dto.PlanRequest;
import com.guardianai.dto.PlanResponse;
import com.guardianai.service.PlannerAgentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agents/planner")
public class PlannerAgentController {

    private final PlannerAgentService plannerAgentService;

    public PlannerAgentController(PlannerAgentService plannerAgentService) {
        this.plannerAgentService = plannerAgentService;
    }

    @PostMapping
    public ResponseEntity<PlanResponse> createExecutionPlan(@Valid @RequestBody PlanRequest request) {
        PlanResponse response = plannerAgentService.createPlan(request);
        return ResponseEntity.ok(response);
    }
}
