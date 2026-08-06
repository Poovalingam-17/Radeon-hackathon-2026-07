package com.guardianai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public HelloResponse getHello() {
        return new HelloResponse("UP", "Hello from GuardianAI Backend!", Instant.now().toString());
    }

    public record HelloResponse(String status, String message, String timestamp) {}
}
