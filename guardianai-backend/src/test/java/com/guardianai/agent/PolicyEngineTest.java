package com.guardianai.agent;

import com.guardianai.dto.PolicyEvaluationResult;
import com.guardianai.model.Policy;
import com.guardianai.model.PolicyRule;
import com.guardianai.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PolicyEngineTest {

    private PolicyRepository policyRepository;
    private PolicyEngine policyEngine;

    @BeforeEach
    public void setUp() {
        policyRepository = Mockito.mock(PolicyRepository.class);
        policyEngine = new PolicyEngine(policyRepository);
    }

    @Test
    public void testEvaluatePass() {
        Policy policy = new Policy();
        policy.setId(1L);
        policy.setName("Test Block Policy");
        policy.setStatus("ACTIVE");
        
        PolicyRule rule = new PolicyRule();
        rule.setName("Contains Blocked Keyword");
        rule.setCondition("contains(\"blocked_keyword\")");
        rule.setAction("DENY");
        rule.setEnabled(true);
        
        policy.setRules(Collections.singletonList(rule));

        when(policyRepository.findAll()).thenReturn(Collections.singletonList(policy));

        PolicyEvaluationResult result = policyEngine.evaluate("This is a clean request prompt.", new HashMap<>());

        assertTrue(result.isCompliant());
        assertEquals("ALLOW", result.getDecision());
    }

    @Test
    public void testEvaluateFail() {
        Policy policy = new Policy();
        policy.setId(1L);
        policy.setName("Test Block Policy");
        policy.setStatus("ACTIVE");
        
        PolicyRule rule = new PolicyRule();
        rule.setName("Contains Blocked Keyword");
        rule.setCondition("contains(\"blocked_keyword\")");
        rule.setAction("DENY");
        rule.setEnabled(true);
        
        policy.setRules(Collections.singletonList(rule));

        when(policyRepository.findAll()).thenReturn(Collections.singletonList(policy));

        PolicyEvaluationResult result = policyEngine.evaluate("This request contains blocked_keyword here.", new HashMap<>());

        assertFalse(result.isCompliant());
        assertEquals("DENY", result.getDecision());
    }
}
