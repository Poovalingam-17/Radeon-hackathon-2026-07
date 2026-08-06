package com.guardianai.controller;

import com.guardianai.agent.SecurityAgent;
import com.guardianai.service.SecurityAgentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agents/security")
public class SecurityAgentController {

    private final SecurityAgentService securityAgentService;

    public SecurityAgentController(SecurityAgentService securityAgentService) {
        this.securityAgentService = securityAgentService;
    }

    @PostMapping
    public ResponseEntity<SecurityAgent.SecurityAgentResult> scanRequest(
            @RequestBody String prompt,
            HttpServletRequest request) {

        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            userAgent = "Unknown";
        }

        SecurityAgent.SecurityAgentResult result = securityAgentService.evaluateCurrentRequest(prompt, ipAddress, userAgent);
        return ResponseEntity.ok(result);
    }
}
