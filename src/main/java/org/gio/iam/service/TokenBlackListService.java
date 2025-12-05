package org.gio.iam.service;

import  lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;



@Service
@RequiredArgsConstructor
public class TokenBlackListService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX = "blacklist:";


    public void blacklistToken(String jti,long expirationEpochSeconds){

        long ttlSeconds = expirationEpochSeconds - (System.currentTimeMillis()/1000);

        if (ttlSeconds > 0){
            redisTemplate.opsForValue().set(PREFIX+jti, "revoked",ttlSeconds,TimeUnit.SECONDS);
        }
    }

    public boolean isBlacklisted(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + jti));
    }
}
