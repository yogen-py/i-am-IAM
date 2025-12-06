package org.gio.iam.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisStartupProbe implements CommandLineRunner {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("🔍 PROBE: Attempting to connect to Redis...");
            redisTemplate.opsForValue().set("probe:status", "connected");
            Object value = redisTemplate.opsForValue().get("probe:status");

            if ("connected".equals(value)) {
                log.info(
                        "✅ PROBE SUCCESS: Successfully wrote and read from Redis! Application <-> Redis connection is HEALTHY.");
            } else {
                log.error("❌ PROBE FAILURE: Wrote to Redis but got unexpected value: {}", value);
            }
        } catch (Exception e) {
            log.error("❌ PROBE EXCEPTION: Could not talk to Redis. Cause: {}", e.getMessage(), e);
        }
    }
}
