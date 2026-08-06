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
public class AuditLogDto {
    private Long id;
    private Long userId;
    private String username;
    private String action;
    private String resource;
    private Long resourceId;
    private String details;
    private String ipAddress;
    private String userAgent;
    private String status;
    private String severity;
    private LocalDateTime timestamp;
}
