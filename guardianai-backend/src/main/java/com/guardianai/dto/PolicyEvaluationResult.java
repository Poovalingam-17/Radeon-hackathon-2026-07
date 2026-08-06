package com.guardianai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyEvaluationResult {
    private boolean compliant;
    private String decision;
    private List<TriggeredRule> triggeredRules;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TriggeredRule {
        private String policyName;
        private String ruleName;
        private String action;
        private String condition;
    }
}
