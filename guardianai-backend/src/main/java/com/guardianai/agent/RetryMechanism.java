package com.guardianai.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
@Slf4j
public class RetryMechanism {

    public <T> T executeWithRetry(Callable<T> action, int maxAttempts, long initialDelayMs) throws Exception {
        int attempt = 0;
        long delay = initialDelayMs;

        while (true) {
            try {
                attempt++;
                return action.call();
            } catch (Exception e) {
                log.warn("Attempt {} failed with error: {}.", attempt, e.getMessage());
                if (attempt >= maxAttempts) {
                    log.error("Max retry attempts ({}) reached. Task failed.", maxAttempts);
                    throw e;
                }
                log.info("Waiting {} ms before next retry attempt...", delay);
                Thread.sleep(delay);
                delay *= 2;
            }
        }
    }
}
