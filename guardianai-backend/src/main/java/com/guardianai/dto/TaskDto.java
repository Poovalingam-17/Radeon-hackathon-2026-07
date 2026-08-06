package com.guardianai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {
    private Long id;
    private String name;
    private String description;
    private String type;
    private String status;
    private String priority;
    private Long agentId;
    private Long userId;
    private String input;
    private String output;
    private String error;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
