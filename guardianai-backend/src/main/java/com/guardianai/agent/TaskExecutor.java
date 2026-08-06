package com.guardianai.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Configuration
@Component
@Slf4j
public class TaskExecutor {

    @Bean(name = "agentTaskExecutor")
    public Executor agentTaskExecutor() {
        log.info("Initializing Agent ThreadPoolTaskExecutor thread pools...");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("ExecutionAgent-");
        executor.initialize();
        return executor;
    }
}
