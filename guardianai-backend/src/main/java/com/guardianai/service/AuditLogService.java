package com.guardianai.service;

import com.guardianai.dto.AuditLogDto;
import com.guardianai.dto.AuditLogSearchRequest;
import com.guardianai.model.AuditLog;
import com.guardianai.repository.AuditLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Async
    @Transactional
    public void saveLogAsync(AuditLog auditLog) {
        log.info("Asynchronously persisting AOP audit log for action: {}", auditLog.getAction());
        auditLogRepository.save(auditLog);
    }

    @Transactional(readOnly = true)
    public List<AuditLogDto> searchLogs(AuditLogSearchRequest request) {
        log.info("Filtering audit logs through search payload criteria");
        return auditLogRepository.findAll().stream()
                .filter(logEntity -> request.getAction() == null || logEntity.getAction().equalsIgnoreCase(request.getAction()))
                .filter(logEntity -> request.getResource() == null || logEntity.getResource().equalsIgnoreCase(request.getResource()))
                .filter(logEntity -> request.getStatus() == null || logEntity.getStatus().equalsIgnoreCase(request.getStatus()))
                .filter(logEntity -> request.getSeverity() == null || logEntity.getSeverity().equalsIgnoreCase(request.getSeverity()))
                .filter(logEntity -> request.getUserId() == null || (logEntity.getUser() != null && logEntity.getUser().getId().equals(request.getUserId())))
                .map(this::mapToAuditLogDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AuditLogDto> getLogsByUserId(Long userId) {
        log.info("Retrieving audit logs by user ID: {}", userId);
        return auditLogRepository.findByUserId(userId).stream()
                .map(this::mapToAuditLogDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AuditLogDto findById(Long id) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Audit log not found with ID: " + id));
        return mapToAuditLogDto(auditLog);
    }

    @Transactional(readOnly = true)
    public byte[] exportLogsToCsv() {
        log.info("Generating CSV report bytes from audit logs...");
        List<AuditLog> logs = auditLogRepository.findAll();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out, true, StandardCharsets.UTF_8)) {
            writer.println("ID,Timestamp,Username,Action,Resource,Status,Severity,IP Address,Details");
            for (AuditLog logEntity : logs) {
                String username = logEntity.getUser() != null ? logEntity.getUser().getUsername() : "system";
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        logEntity.getId(),
                        logEntity.getTimestamp().toString(),
                        username,
                        escapeCsv(logEntity.getAction()),
                        escapeCsv(logEntity.getResource()),
                        escapeCsv(logEntity.getStatus()),
                        escapeCsv(logEntity.getSeverity()),
                        escapeCsv(logEntity.getIpAddress()),
                        escapeCsv(logEntity.getDetails())
                );
            }
        }
        return out.toByteArray();
    }

    private String escapeCsv(String val) {
        if (val == null) return "";
        return "\"" + val.replace("\"", "\"\"") + "\"";
    }

    private AuditLogDto mapToAuditLogDto(AuditLog logEntity) {
        return AuditLogDto.builder()
                .id(logEntity.getId())
                .userId(logEntity.getUser() != null ? logEntity.getUser().getId() : null)
                .username(logEntity.getUser() != null ? logEntity.getUser().getUsername() : "system")
                .action(logEntity.getAction())
                .resource(logEntity.getResource())
                .resourceId(logEntity.getResourceId())
                .details(logEntity.getDetails())
                .ipAddress(logEntity.getIpAddress())
                .userAgent(logEntity.getUserAgent())
                .status(logEntity.getStatus())
                .severity(logEntity.getSeverity())
                .timestamp(logEntity.getTimestamp())
                .build();
    }
}
