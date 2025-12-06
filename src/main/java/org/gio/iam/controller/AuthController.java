package org.gio.iam.controller;

import lombok.RequiredArgsConstructor;
import org.gio.iam.service.TokenBlackListService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenBlackListService tokenBlacklistService;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Jwt jwt) {
        // 1. Safe Extraction
        String jti = jwt.getId();
        Instant expiresAt = jwt.getExpiresAt();

        // 2. Defensive Check: If token has no expiration (unlikely with Keycloak but possible),
        // we cannot calculate a TTL. In this case, we can reject it or ignore it.
        if (expiresAt == null) {
            return ResponseEntity.badRequest().build();
        }

        // 3. Send to Redis
        tokenBlacklistService.blacklistToken(jti, expiresAt.getEpochSecond());

        return ResponseEntity.noContent().build();
    }
}