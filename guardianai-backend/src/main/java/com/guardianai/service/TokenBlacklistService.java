package com.guardianai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;

    public TokenBlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklistToken(String token, long expirationMs) {
        try {
            redisTemplate.opsForValue().set(token, "blacklisted", expirationMs, TimeUnit.MILLISECONDS);
            log.info("Token successfully blacklisted for {} ms", expirationMs);
        } catch (Exception e) {
            log.error("Failed to store blacklisted token in Redis: {}", e.getMessage());
        }
    }

    public boolean isBlacklisted(String token) {
        try {
            Boolean hasKey = redisTemplate.hasKey(token);
            return hasKey != null && hasKey;
        } catch (Exception e) {
            log.error("Failed to query blacklist from Redis: {}", e.getMessage());
            return false;
        }
    }
}
