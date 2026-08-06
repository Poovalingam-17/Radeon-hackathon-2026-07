package com.guardianai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTaskRequest {
    @NotBlank(message = "Task name is required")
    private String name;

    private String description;

    @NotBlank(message = "Task type is required")
    private String type;

    private String priority;

    private Long agentId;

    private String input;
}
