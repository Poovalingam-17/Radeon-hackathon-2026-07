package com.guardianai.security;

import com.guardianai.model.AuditLog;
import com.guardianai.model.User;
import com.guardianai.model.UserPrincipal;
import com.guardianai.repository.UserRepository;
import com.guardianai.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AuditAspect {

    private final AuditLogService auditLogService;
    private final UserRepository userRepository;

    public AuditAspect(AuditLogService auditLogService, UserRepository userRepository) {
        this.auditLogService = auditLogService;
        this.userRepository = userRepository;
    }

    @Around("@annotation(auditAction)")
    public Object audit(ProceedingJoinPoint joinPoint, AuditAction auditAction) throws Throwable {
        log.info("AOP Auditing method: {}", joinPoint.getSignature().toShortString());

        String status = "SUCCESS";
        String details = "Method execution completed successfully.";
        Object result = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable t) {
            status = "FAILED";
            details = "Failed with error: " + t.getMessage();
            throw t;
        } finally {
            try {
                HttpServletRequest request = null;
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    request = attributes.getRequest();
                }

                String ipAddress = request != null ? request.getRemoteAddr() : "system";
                String userAgent = request != null ? request.getHeader("User-Agent") : "system";

                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                User user = null;
                if (principal instanceof UserPrincipal userPrincipal) {
                    user = userRepository.findById(userPrincipal.getId()).orElse(null);
                }

                AuditLog logEntity = AuditLog.builder()
                        .action(auditAction.action())
                        .resource(auditAction.resource())
                        .severity(auditAction.severity())
                        .status(status)
                        .details(details)
                        .ipAddress(ipAddress)
                        .userAgent(userAgent)
                        .timestamp(LocalDateTime.now())
                        .user(user)
                        .build();

                auditLogService.saveLogAsync(logEntity);
            } catch (Exception ex) {
                log.error("Failed to write AOP audit log: {}", ex.getMessage());
            }
        }
    }
}
