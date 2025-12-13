package org.gio.iam.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

// This config class will be loaded only during tests
@TestConfiguration
public class TestSecurityConfig {

    // Provide a mock JwtDecoder bean to satisfy Spring Security's requirements
    @Bean
    public JwtDecoder jwtDecoder() {
        // We use a basic NimbusJwtDecoder setup with a fake URI.
        // The context will load, but the decoder is never actually used by RedisConnectionTest.
        return NimbusJwtDecoder.withJwkSetUri("http://localhost/non-existent-uri").build();
    }
}