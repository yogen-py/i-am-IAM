package org.gio.iam.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RedisConnectionTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisWriteAndRead() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.out.println("🧪 TEST: Attempting Redis connection...");
        try {
            redisTemplate.opsForValue().set("test:key", "test_value");
            Object value = redisTemplate.opsForValue().get("test:key");

            System.out.println("✅ TEST SUCCESS: Retrieved value: " + value);
            assertEquals("test_value", value);
        } catch (Exception e) {
            System.err.println("❌ TEST FAILURE: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
