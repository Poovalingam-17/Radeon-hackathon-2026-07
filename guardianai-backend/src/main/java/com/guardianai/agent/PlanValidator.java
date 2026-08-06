package com.guardianai.agent;

import com.guardianai.dto.PlanResponse;
import com.guardianai.dto.PlanResponse.PlanStepDto;
import org.springframework.stereotype.Component;

@Component
public class PlanValidator {

    public boolean validate(PlanResponse plan) {
        if (plan.getIntent() == null || plan.getIntent().equals("UNKNOWN")) {
            return false;
        }
        if (plan.getSteps() == null || plan.getSteps().isEmpty()) {
            return false;
        }
        int expectedOrder = 1;
        for (PlanStepDto step : plan.getSteps()) {
            if (step.getOrderIndex() != expectedOrder) {
                return false;
            }
            expectedOrder++;
        }
        return true;
    }
}
