package com.guardianai.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ShortTermMemory {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public ShortTermMemory(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void appendMessage(Long userId, String role, String content) {
        String key = "user:" + userId + ":shortterm";
        try {
            Message message = new Message(role, content, System.currentTimeMillis());
            String json = objectMapper.writeValueAsString(message);
            redisTemplate.opsForList().rightPush(key, json);
            redisTemplate.expire(key, 1, TimeUnit.HOURS);
            log.info("Appended message to short-term memory key: {}", key);
        } catch (Exception e) {
            log.error("Failed to append message to Redis short-term memory: {}", e.getMessage());
        }
    }

    public List<Message> getHistory(Long userId) {
        String key = "user:" + userId + ":shortterm";
        List<Message> history = new ArrayList<>();
        try {
            List<String> rawMessages = redisTemplate.opsForList().range(key, 0, -1);
            if (rawMessages != null) {
                for (String raw : rawMessages) {
                    history.add(objectMapper.readValue(raw, Message.class));
                }
            }
        } catch (Exception e) {
            log.error("Failed to fetch history from Redis short-term memory: {}", e.getMessage());
        }
        return history;
    }

    public void clearHistory(Long userId) {
        String key = "user:" + userId + ":shortterm";
        try {
            redisTemplate.delete(key);
            log.info("Cleared short-term memory key: {}", key);
        } catch (Exception e) {
            log.error("Failed to clear short-term memory: {}", e.getMessage());
        }
    }

    public record Message(String role, String content, long timestamp) {
        public Message() {
            this("user", "", 0L);
        }
    }
}
