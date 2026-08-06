package com.guardianai.service;

import com.guardianai.agent.PolicyEngine;
import com.guardianai.dto.CreatePolicyRequest;
import com.guardianai.dto.PolicyDto;
import com.guardianai.dto.PolicyEvaluationResult;
import com.guardianai.dto.UpdatePolicyRequest;
import com.guardianai.model.Policy;
import com.guardianai.model.PolicyRule;
import com.guardianai.model.UserPrincipal;
import com.guardianai.repository.PolicyRepository;
import com.guardianai.repository.PolicyRuleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyRuleRepository policyRuleRepository;
    private final PolicyEngine policyEngine;

    public PolicyService(PolicyRepository policyRepository, PolicyRuleRepository policyRuleRepository, PolicyEngine policyEngine) {
        this.policyRepository = policyRepository;
        this.policyRuleRepository = policyRuleRepository;
        this.policyEngine = policyEngine;
    }

    @Transactional(readOnly = true)
    public List<PolicyDto> findAll() {
        log.info("Fetching all policies");
        return policyRepository.findAll().stream().map(this::mapToPolicyDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PolicyDto findById(Long id) {
        log.info("Fetching policy details for ID: {}", id);
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Policy not found with ID: " + id));
        return mapToPolicyDto(policy);
    }

    @Transactional
    public PolicyDto create(CreatePolicyRequest request) {
        log.info("Creating policy: {}", request.getName());
        if (policyRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Policy name is already taken");
        }

        String username = getCurrentUsername();

        Policy policy = Policy.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .priority(request.getPriority())
                .version("1.0.0")
                .createdBy(username)
                .effectiveDate(LocalDateTime.now())
                .rules(new ArrayList<>())
                .build();

        Policy savedPolicy = policyRepository.save(policy);

        if (request.getRules() != null) {
            for (CreatePolicyRequest.RuleDto rDto : request.getRules()) {
                PolicyRule rule = PolicyRule.builder()
                        .name(rDto.getName())
                        .description(rDto.getDescription())
                        .condition(rDto.getCondition())
                        .action(rDto.getAction())
                        .priority(rDto.getPriority())
                        .enabled(rDto.isEnabled())
                        .policy(savedPolicy)
                        .build();
                savedPolicy.getRules().add(policyRuleRepository.save(rule));
            }
        }

        return mapToPolicyDto(savedPolicy);
    }

    @Transactional
    public PolicyDto update(Long id, UpdatePolicyRequest request) {
        log.info("Updating policy ID: {}", id);
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Policy not found with ID: " + id));

        if (!policy.getName().equals(request.getName()) && policyRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Policy name is already taken");
        }

        String currentVersion = policy.getVersion();
        String nextVersion = bumpVersionMinor(currentVersion);

        policy.setName(request.getName());
        policy.setDescription(request.getDescription());
        policy.setType(request.getType());
        policy.setStatus(request.getStatus());
        policy.setPriority(request.getPriority());
        policy.setVersion(nextVersion);

        policyRuleRepository.deleteAll(policy.getRules());
        policy.getRules().clear();

        Policy savedPolicy = policyRepository.save(policy);

        if (request.getRules() != null) {
            for (CreatePolicyRequest.RuleDto rDto : request.getRules()) {
                PolicyRule rule = PolicyRule.builder()
                        .name(rDto.getName())
                        .description(rDto.getDescription())
                        .condition(rDto.getCondition())
                        .action(rDto.getAction())
                        .priority(rDto.getPriority())
                        .enabled(rDto.isEnabled())
                        .policy(savedPolicy)
                        .build();
                savedPolicy.getRules().add(policyRuleRepository.save(rule));
            }
        }

        return mapToPolicyDto(savedPolicy);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting policy with ID: {}", id);
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Policy not found with ID: " + id));
        policyRepository.delete(policy);
    }

    public PolicyEvaluationResult evaluate(String prompt, Map<String, Object> payload) {
        return policyEngine.evaluate(prompt, payload);
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getUsername();
        }
        return "system";
    }

    private String bumpVersionMinor(String version) {
        try {
            String[] parts = version.split("\\.");
            int minor = Integer.parseInt(parts[1]) + 1;
            return parts[0] + "." + minor + ".0";
        } catch (Exception e) {
            return "1.0.0";
        }
    }

    private PolicyDto mapToPolicyDto(Policy policy) {
        List<PolicyDto.RuleDto> rules = policy.getRules().stream()
                .map(r -> PolicyDto.RuleDto.builder()
                        .id(r.getId())
                        .name(r.getName())
                        .description(r.getDescription())
                        .condition(r.getCondition())
                        .action(r.getAction())
                        .priority(r.getPriority())
                        .enabled(r.isEnabled())
                        .build())
                .collect(Collectors.toList());

        return PolicyDto.builder()
                .id(policy.getId())
                .name(policy.getName())
                .description(policy.getDescription())
                .type(policy.getType())
                .status(policy.getStatus())
                .priority(policy.getPriority())
                .version(policy.getVersion())
                .createdBy(policy.getCreatedBy())
                .rules(rules)
                .build();
    }
}
