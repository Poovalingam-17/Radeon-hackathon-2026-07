package com.guardianai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogSearchRequest {
    private String query;
    private Long userId;
    private String action;
    private String resource;
    private String status;
    private String severity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
