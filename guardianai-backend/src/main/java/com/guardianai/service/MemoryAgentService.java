package com.guardianai.service;

import com.guardianai.agent.MemoryAgent;
import com.guardianai.model.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MemoryAgentService {

    private final MemoryAgent memoryAgent;

    public MemoryAgentService(MemoryAgent memoryAgent) {
        this.memoryAgent = memoryAgent;
    }

    public MemoryAgent.MemoryContext retrieveCurrentContext(String prompt) {
        log.info("Dispatching prompt to MemoryAgent context fetcher");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = 1L;
        if (principal instanceof UserPrincipal userPrincipal) {
            userId = userPrincipal.getId();
        }
        return memoryAgent.retrieveContext(userId, prompt);
    }
}
