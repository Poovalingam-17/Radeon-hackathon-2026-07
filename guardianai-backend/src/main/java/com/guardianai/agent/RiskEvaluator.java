package com.guardianai.agent;

import com.guardianai.model.RiskScore;
import com.guardianai.model.User;
import com.guardianai.repository.RiskScoreRepository;
import com.guardianai.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Slf4j
public class RiskEvaluator {

    private final RiskScoreRepository riskScoreRepository;
    private final UserRepository userRepository;

    public RiskEvaluator(RiskScoreRepository riskScoreRepository, UserRepository userRepository) {
        this.riskScoreRepository = riskScoreRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RiskEvaluationResult evaluate(Long userId, ThreatDetector.ThreatScanResult threatResult, AnomalyDetector.AnomalyScanResult anomalyResult) {
        log.info("Calculating risk assessment for user ID: {}", userId);

        double totalContribution = threatResult.riskContribution() + anomalyResult.riskContribution();
        double normalizedRisk = Math.min(1.0, Math.max(0.0, totalContribution));

        double scaleScore = normalizedRisk * 100.0;
        String level = "LOW";
        if (scaleScore > 75) {
            level = "CRITICAL";
        } else if (scaleScore > 50) {
            level = "HIGH";
        } else if (scaleScore > 25) {
            level = "MEDIUM";
        }

        StringBuilder factors = new StringBuilder();
        if (!threatResult.isSafe()) {
            factors.append("Active prompt injection threat flagged (").append(String.join(", ", threatResult.threats())).append("). ");
        }
        if (!anomalyResult.isNormal()) {
            factors.append("Outlier transaction parameters detected (").append(String.join(", ", anomalyResult.anomalies())).append("). ");
        }
        if (factors.isEmpty()) {
            factors.append("No active vulnerabilities or pattern anomalies identified.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        RiskScore score = RiskScore.builder()
                .user(user)
                .score(scaleScore)
                .level(level)
                .factors(factors.toString())
                .context("Security Agent Scan Assessment")
                .timestamp(LocalDateTime.now())
                .build();

        riskScoreRepository.save(score);
        log.info("Risk assessment generated: level={}, score={}", level, scaleScore);

        return new RiskEvaluationResult(scaleScore, level, factors.toString());
    }

    public record RiskEvaluationResult(double score, String level, String factors) {}
}
