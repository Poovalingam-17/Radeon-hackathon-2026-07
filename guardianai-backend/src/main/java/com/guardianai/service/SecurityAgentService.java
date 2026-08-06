package com.guardianai.service;

import com.guardianai.agent.SecurityAgent;
import com.guardianai.model.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SecurityAgentService {

    private final SecurityAgent securityAgent;

    public SecurityAgentService(SecurityAgent securityAgent) {
        this.securityAgent = securityAgent;
    }

    public SecurityAgent.SecurityAgentResult evaluateCurrentRequest(String prompt, String ipAddress, String userAgent) {
        log.info("Dispatching prompt to SecurityAgent check");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = 1L;
        if (principal instanceof UserPrincipal userPrincipal) {
            userId = userPrincipal.getId();
        }
        return securityAgent.evaluateRequest(userId, prompt, ipAddress, userAgent);
    }
}
