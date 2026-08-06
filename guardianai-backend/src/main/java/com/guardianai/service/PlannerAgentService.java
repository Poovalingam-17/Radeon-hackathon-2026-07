package com.guardianai.service;

import com.guardianai.agent.PlannerAgent;
import com.guardianai.dto.PlanRequest;
import com.guardianai.dto.PlanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PlannerAgentService {

    private final PlannerAgent plannerAgent;

    public PlannerAgentService(PlannerAgent plannerAgent) {
        this.plannerAgent = plannerAgent;
    }

    public PlanResponse createPlan(PlanRequest request) {
        log.info("Dispatching prompt to PlannerAgent engine: {}", request.getPrompt());
        return plannerAgent.plan(request.getPrompt());
    }
}
