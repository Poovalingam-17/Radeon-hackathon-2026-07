package com.guardianai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePolicyRequest {
    @NotBlank(message = "Policy name is required")
    @Size(min = 3, max = 100, message = "Policy name must be between 3 and 100 characters")
    private String name;

    private String description;

    @NotBlank(message = "Policy type is required (e.g. SAFETY, PRIVACY, COMPLIANCE)")
    private String type;

    private String status;

    private int priority;

    private List<RuleDto> rules;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RuleDto {
        @NotBlank(message = "Rule name is required")
        private String name;
        private String description;
        @NotBlank(message = "Condition expression is required")
        private String condition;
        @NotBlank(message = "Rule action is required (e.g. ALLOW, DENY, FLAG)")
        private String action;
        private int priority;
        private boolean enabled;
    }
}
