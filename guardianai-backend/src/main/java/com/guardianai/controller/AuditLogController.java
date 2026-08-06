package com.guardianai.controller;

import com.guardianai.dto.AuditLogDto;
import com.guardianai.dto.AuditLogSearchRequest;
import com.guardianai.service.AuditLogService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_AUDIT')")
    public ResponseEntity<List<AuditLogDto>> getAllAuditLogs() {
        return ResponseEntity.ok(auditLogService.searchLogs(new AuditLogSearchRequest()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_AUDIT')")
    public ResponseEntity<AuditLogDto> getAuditLogById(@PathVariable Long id) {
        return ResponseEntity.ok(auditLogService.findById(id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('READ_AUDIT')")
    public ResponseEntity<List<AuditLogDto>> getAuditLogsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(auditLogService.getLogsByUserId(userId));
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('READ_AUDIT')")
    public ResponseEntity<List<AuditLogDto>> searchAuditLogs(@RequestBody AuditLogSearchRequest request) {
        return ResponseEntity.ok(auditLogService.searchLogs(request));
    }

    @GetMapping("/export")
    @PreAuthorize("hasAuthority('READ_AUDIT')")
    public ResponseEntity<byte[]> exportAuditLogs() {
        byte[] csvData = auditLogService.exportLogsToCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=audit_logs.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }
}
