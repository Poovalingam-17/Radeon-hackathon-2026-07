package com.guardianai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyEvaluationRequest {
    @NotBlank(message = "Prompt context is required")
    private String prompt;

    private Map<String, Object> payload;
}
