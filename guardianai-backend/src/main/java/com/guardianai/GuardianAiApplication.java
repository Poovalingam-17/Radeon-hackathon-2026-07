package com.guardianai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableAspectJAutoProxy
public class GuardianAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuardianAiApplication.class, args);
    }
}
