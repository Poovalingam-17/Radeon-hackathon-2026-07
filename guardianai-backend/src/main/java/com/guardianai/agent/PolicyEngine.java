package com.guardianai.agent;

import com.guardianai.dto.PolicyEvaluationResult;
import com.guardianai.dto.PolicyEvaluationResult.TriggeredRule;
import com.guardianai.model.Policy;
import com.guardianai.model.PolicyRule;
import com.guardianai.repository.PolicyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class PolicyEngine {

    private final PolicyRepository policyRepository;

    public PolicyEngine(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    public PolicyEvaluationResult evaluate(String prompt, Map<String, Object> payload) {
        log.info("Policy Engine evaluating prompt compliance...");
        List<TriggeredRule> triggered = new ArrayList<>();

        List<Policy> activePolicies = policyRepository.findAll().stream()
                .filter(p -> "ACTIVE".equalsIgnoreCase(p.getStatus()))
                .toList();

        for (Policy policy : activePolicies) {
            for (PolicyRule rule : policy.getRules()) {
                if (!rule.isEnabled()) continue;

                if (evaluateCondition(rule.getCondition(), prompt, payload)) {
                    log.warn("Rule matched target condition: {} in Policy: {}", rule.getName(), policy.getName());
                    triggered.add(TriggeredRule.builder()
                            .policyName(policy.getName())
                            .ruleName(rule.getName())
                            .action(rule.getAction())
                            .condition(rule.getCondition())
                            .build());
                }
            }
        }

        boolean compliant = true;
        String finalDecision = "ALLOW";

        for (TriggeredRule tr : triggered) {
            if ("DENY".equalsIgnoreCase(tr.getAction())) {
                compliant = false;
                finalDecision = "DENY";
                break;
            } else if ("FLAG".equalsIgnoreCase(tr.getAction())) {
                finalDecision = "FLAG";
            }
        }

        return PolicyEvaluationResult.builder()
                .compliant(compliant)
                .decision(finalDecision)
                .triggeredRules(triggered)
                .build();
    }

    private boolean evaluateCondition(String condition, String prompt, Map<String, Object> payload) {
        if (condition.startsWith("contains(")) {
            String keyword = condition.substring(10, condition.length() - 2);
            return prompt.toLowerCase().contains(keyword.toLowerCase());
        }
        if (condition.startsWith("matches(")) {
            String regex = condition.substring(9, condition.length() - 2);
            try {
                return prompt.matches(regex);
            } catch (Exception e) {
                log.error("Invalid regex in policy condition: {}", regex);
                return false;
            }
        }
        if (condition.contains("==") && payload != null) {
            String[] parts = condition.split("==");
            String key = parts[0].trim();
            String value = parts[1].trim().replace("\"", "");
            Object payloadVal = payload.get(key);
            return payloadVal != null && payloadVal.toString().equalsIgnoreCase(value);
        }

        return false;
    }
}
