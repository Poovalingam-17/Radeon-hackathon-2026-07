package com.guardianai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardActivityDto {
    private Long id;
    private String action;
    private String timestamp;
    private String details;
    private String severity;
}
