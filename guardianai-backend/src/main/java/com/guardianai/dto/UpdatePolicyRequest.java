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
public class UpdatePolicyRequest {
    @NotBlank(message = "Policy name is required")
    @Size(min = 3, max = 100, message = "Policy name must be between 3 and 100 characters")
    private String name;

    private String description;

    @NotBlank(message = "Policy type is required")
    private String type;

    private String status;

    private int priority;

    private List<CreatePolicyRequest.RuleDto> rules;
}
