package com.guardianai.security;

import com.guardianai.repository.UserRepository;
import com.guardianai.service.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuditAspectTest {

    private AuditLogService auditLogService;
    private UserRepository userRepository;
    private AuditAspect auditAspect;

    @BeforeEach
    public void setUp() {
        auditLogService = Mockito.mock(AuditLogService.class);
        userRepository = Mockito.mock(UserRepository.class);
        auditAspect = new AuditAspect(auditLogService, userRepository);
    }

    @Test
    public void testAuditAspectConfigured() {
        assertNotNull(auditAspect);
    }
}
