package com.guardianai.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecurityAgent {

    private final ThreatDetector threatDetector;
    private final AnomalyDetector anomalyDetector;
    private final RiskEvaluator riskEvaluator;

    public SecurityAgent(ThreatDetector threatDetector, AnomalyDetector anomalyDetector, RiskEvaluator riskEvaluator) {
        this.threatDetector = threatDetector;
        this.anomalyDetector = anomalyDetector;
        this.riskEvaluator = riskEvaluator;
    }

    public SecurityAgentResult evaluateRequest(Long userId, String prompt, String ipAddress, String userAgent) {
        log.info("Security Agent starting analysis for user: {}", userId);

        ThreatDetector.ThreatScanResult threatResult = threatDetector.scan(prompt);
        AnomalyDetector.AnomalyScanResult anomalyResult = anomalyDetector.scan(userId, ipAddress, userAgent);
        RiskEvaluator.RiskEvaluationResult riskResult = riskEvaluator.evaluate(userId, threatResult, anomalyResult);

        boolean passed = threatResult.isSafe() && anomalyResult.isNormal() && riskResult.score() < 50;

        return new SecurityAgentResult(passed, riskResult.score(), riskResult.level(), riskResult.factors());
    }

    public record SecurityAgentResult(boolean passed, double riskScore, String rating, String factors) {}
}
