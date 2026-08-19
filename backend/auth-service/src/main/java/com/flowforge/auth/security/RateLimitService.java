package com.flowforge.auth.security;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    private final StringRedisTemplate redisTemplate;

    private static final int MAX_ATTEMPTS = 5;

    private static final Duration WINDOW =
            Duration.ofHours(1);

    public RateLimitService(
            StringRedisTemplate redisTemplate) {

        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String key) {

        // it  check count of request that hit
        Long count =
                redisTemplate.opsForValue().increment(key);

        if (count == null) {
            return false;
        }

        if (count == 1) {
            redisTemplate.expire(
                    key,
                    WINDOW
            );
        }

        return count <= MAX_ATTEMPTS;
    }
}