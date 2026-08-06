package com.guardianai.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class AnomalyDetector {

    private final StringRedisTemplate redisTemplate;

    public AnomalyDetector(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public AnomalyScanResult scan(Long userId, String ipAddress, String userAgent) {
        log.info("Anomaly scan triggered for user: {} from IP: {}", userId, ipAddress);
        List<String> anomalies = new ArrayList<>();

        String rateKey = "user:" + userId + ":rate:" + (System.currentTimeMillis() / 1000);
        try {
            Long count = redisTemplate.opsForValue().increment(rateKey, 1);
            if (count != null && count == 1) {
                redisTemplate.expire(rateKey, 5, TimeUnit.SECONDS);
            }
            if (count != null && count > 5) {
                log.warn("User {} exceeded request rate limits: {} req/s", userId, count);
                anomalies.add("RATE_LIMIT_EXCEEDED");
            }
        } catch (Exception e) {
            log.error("Redis rate check failed: {}", e.getMessage());
        }

        String ipKey = "user:" + userId + ":ip";
        try {
            String lastIp = redisTemplate.opsForValue().get(ipKey);
            if (lastIp != null && !lastIp.equals(ipAddress)) {
                log.warn("User {} IP changed rapidly from {} to {}", userId, lastIp, ipAddress);
                anomalies.add("GEOGRAPHIC_IP_SHIFT");
            }
            redisTemplate.opsForValue().set(ipKey, ipAddress, 24, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("Redis IP footprint track failed: {}", e.getMessage());
        }

        double riskScoreContribution = anomalies.isEmpty() ? 0.0 : (anomalies.contains("RATE_LIMIT_EXCEEDED") ? 0.4 : 0.2);
        return new AnomalyScanResult(anomalies.isEmpty(), anomalies, riskScoreContribution);
    }

    public record AnomalyScanResult(boolean isNormal, List<String> anomalies, double riskContribution) {}
}
