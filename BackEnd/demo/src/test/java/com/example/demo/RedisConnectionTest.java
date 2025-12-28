package com.example.demo;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;

@TestComponent
public class RedisConnectionTest {

    private static final Logger log = LoggerFactory.getLogger(RedisConnectionTest.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void testRedisConnection() {
        try {
            redisTemplate.opsForValue().set("connection-test", "success", Duration.ofSeconds(10));
            String result = (String) redisTemplate.opsForValue().get("connection-test");
            if ("success".equals(result)) {
                log.info("✅ Redis connection test successful");
            } else {
                log.error("❌ Redis connection test failed");
            }
        } catch (Exception e) {
            log.error("❌ Redis connection exception: {}", e.getMessage());
        }
    }
}
