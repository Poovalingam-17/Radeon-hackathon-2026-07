package com.guardianai.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Configuration
@EnableScheduling
@Component
@Slf4j
public class TaskScheduler {

    @Scheduled(fixedRate = 30000)
    public void runHeartbeatTasks() {
        log.info("Execution Agent scheduling heartbeat task checking active workers...");
    }
}
