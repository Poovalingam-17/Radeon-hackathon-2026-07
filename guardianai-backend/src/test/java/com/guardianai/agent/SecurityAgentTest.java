package com.guardianai.agent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityAgentTest {

    private ThreatDetector threatDetector;
    private AnomalyDetector anomalyDetector;
    private RiskEvaluator riskEvaluator;
    private SecurityAgent securityAgent;

    @BeforeEach
    public void setUp() {
        threatDetector = Mockito.mock(ThreatDetector.class);
        anomalyDetector = Mockito.mock(AnomalyDetector.class);
        riskEvaluator = Mockito.mock(RiskEvaluator.class);
        securityAgent = new SecurityAgent(threatDetector, anomalyDetector, riskEvaluator);
    }

    @Test
    public void testThreatValidation() {
        assertNotNull(securityAgent);
    }
}
