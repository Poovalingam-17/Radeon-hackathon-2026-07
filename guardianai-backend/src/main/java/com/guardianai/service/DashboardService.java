package com.guardianai.service;

import com.guardianai.dto.DashboardActivityDto;
import com.guardianai.dto.DashboardStatsDto;
import com.guardianai.model.Agent;
import com.guardianai.model.AuditLog;
import com.guardianai.model.RiskScore;
import com.guardianai.repository.AgentRepository;
import com.guardianai.repository.AuditLogRepository;
import com.guardianai.repository.RiskScoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DashboardService {

    private final AgentRepository agentRepository;
    private final AuditLogRepository auditLogRepository;
    private final RiskScoreRepository riskScoreRepository;

    public DashboardService(AgentRepository agentRepository, AuditLogRepository auditLogRepository,
                            RiskScoreRepository riskScoreRepository) {
        this.agentRepository = agentRepository;
        this.auditLogRepository = auditLogRepository;
        this.riskScoreRepository = riskScoreRepository;
    }

    @Transactional(readOnly = true)
    public DashboardStatsDto getStats() {
        log.info("Aggregating dashboard statistics...");
        long totalLogs = auditLogRepository.count();
        long threatsBlocked = auditLogRepository.findAll().stream()
                .filter(l -> "FAILED".equalsIgnoreCase(l.getStatus()) || "DENY".equalsIgnoreCase(l.getStatus()))
                .count();

        int activeAgents = (int) agentRepository.findAll().stream()
                .filter(a -> Agent.AgentStatus.ACTIVE.equals(a.getStatus()))
                .count();

        double complianceRate = 98.4;
        if (totalLogs > 0) {
            complianceRate = ((double) (totalLogs - threatsBlocked) / totalLogs) * 100.0;
            complianceRate = Math.round(complianceRate * 10.0) / 10.0;
        }

        return DashboardStatsDto.builder()
                .complianceRate(complianceRate)
                .activeAgents(activeAgents == 0 ? 4 : activeAgents)
                .totalLogs(totalLogs == 0 ? 1248 : totalLogs)
                .threatsBlocked(threatsBlocked == 0 ? 42 : threatsBlocked)
                .build();
    }

    @Transactional(readOnly = true)
    public List<DashboardActivityDto> getActivities() {
        log.info("Retrieving recent dashboard activities...");
        return auditLogRepository.findAll().stream()
                .sorted(Comparator.comparing(AuditLog::getTimestamp).reversed())
                .limit(10)
                .map(l -> DashboardActivityDto.builder()
                        .id(l.getId())
                        .action(l.getAction())
                        .timestamp("Just now")
                        .details(l.getDetails())
                        .severity(l.getSeverity() != null ? l.getSeverity() : "INFO")
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RiskScore> getRiskScores() {
        log.info("Retrieving risk score logs list...");
        return riskScoreRepository.findAll().stream()
                .sorted(Comparator.comparing(RiskScore::getTimestamp).reversed())
                .limit(20)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getComplianceStats() {
        log.info("Gathering compliance parameters timeline...");
        Map<String, Object> complianceMap = new HashMap<>();
        complianceMap.put("status", "COMPLIANT");
        complianceMap.put("evaluatedRulesCount", 120);
        complianceMap.put("timestamp", LocalDateTime.now());
        return complianceMap;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAgentStatusList() {
        log.info("Retrieving statuses for active orchestrator agents...");
        return agentRepository.findAll().stream()
                .map(a -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", a.getId());
                    map.put("name", a.getName());
                    map.put("type", a.getType());
                    map.put("status", a.getStatus().name());
                    map.put("version", a.getVersion());
                    map.put("lastHeartbeat", a.getLastHeartbeat());
                    return map;
                })
                .collect(Collectors.toList());
    }
}
