package com.guardianai.dto;

import com.guardianai.model.AgentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanResponse {
    private String intent;
    private double confidence;
    private List<PlanStepDto> steps;
    private String status;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlanStepDto {
        private int orderIndex;
        private AgentType agentType;
        private String action;
        private String description;
    }
}
