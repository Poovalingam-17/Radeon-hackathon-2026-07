package com.guardianai.controller;

import com.guardianai.dto.CreatePolicyRequest;
import com.guardianai.dto.PolicyDto;
import com.guardianai.dto.PolicyEvaluationRequest;
import com.guardianai.dto.PolicyEvaluationResult;
import com.guardianai.dto.UpdatePolicyRequest;
import com.guardianai.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_POLICY')")
    public ResponseEntity<List<PolicyDto>> getAllPolicies() {
        return ResponseEntity.ok(policyService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_POLICY')")
    public ResponseEntity<PolicyDto> getPolicyById(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_POLICY')")
    public ResponseEntity<PolicyDto> createPolicy(@Valid @RequestBody CreatePolicyRequest request) {
        return ResponseEntity.ok(policyService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_POLICY')")
    public ResponseEntity<PolicyDto> updatePolicy(@PathVariable Long id, @Valid @RequestBody UpdatePolicyRequest request) {
        return ResponseEntity.ok(policyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_POLICY')")
    public ResponseEntity<Void> deletePolicy(@PathVariable Long id) {
        policyService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/evaluate")
    @PreAuthorize("hasAuthority('READ_POLICY')")
    public ResponseEntity<PolicyEvaluationResult> evaluatePolicy(
            @PathVariable Long id,
            @Valid @RequestBody PolicyEvaluationRequest request) {
        PolicyEvaluationResult result = policyService.evaluate(request.getPrompt(), request.getPayload());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/versions/{id}")
    @PreAuthorize("hasAuthority('READ_POLICY')")
    public ResponseEntity<PolicyDto> getPolicyVersionDetails(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.findById(id));
    }
}
