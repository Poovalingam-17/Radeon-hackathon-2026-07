package com.guardianai.agent;

import com.guardianai.dto.PlanResponse;
import org.springframework.stereotype.Component;

@Component
public class PlanOptimizer {

    public PlanResponse optimize(PlanResponse plan) {
        if (plan.getConfidence() > 0.9) {
            plan.setStatus("OPTIMIZED");
        }
        return plan;
    }
}
