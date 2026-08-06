package com.guardianai.agent;

import com.guardianai.model.AuditLog;
import com.guardianai.model.RiskScore;
import com.guardianai.repository.AuditLogRepository;
import com.guardianai.repository.RiskScoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LongTermMemory {

    private final AuditLogRepository auditLogRepository;
    private final RiskScoreRepository riskScoreRepository;

    public LongTermMemory(AuditLogRepository auditLogRepository, RiskScoreRepository riskScoreRepository) {
        this.auditLogRepository = auditLogRepository;
        this.riskScoreRepository = riskScoreRepository;
    }

    public List<String> getHistoricalContext(Long userId) {
        log.info("Fetching long-term memory context for user ID: {}", userId);

        List<AuditLog> logs = auditLogRepository.findByUserIdOrderByTimestampDesc(userId, PageRequest.of(0, 5));
        List<String> auditSummaries = logs.stream()
                .map(l -> String.format("[%s] Action: %s, Resource: %s, Status: %s", l.getTimestamp(), l.getAction(), l.getResource(), l.getStatus()))
                .collect(Collectors.toList());

        List<RiskScore> risks = riskScoreRepository.findByUserIdOrderByTimestampDesc(userId);
        List<String> riskSummaries = risks.stream()
                .limit(3)
                .map(r -> String.format("[%s] Risk Level: %s, Score: %s, Factors: %s", r.getTimestamp(), r.getLevel(), r.getScore(), r.getFactors()))
                .collect(Collectors.toList());

        auditSummaries.addAll(riskSummaries);
        return auditSummaries;
    }
}
