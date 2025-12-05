package org.gio.iam.controller;

import lombok.RequiredArgsConstructor;
import org.gio.iam.service.TokenBlackListService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenBlackListService tokenBlacklistService;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Jwt jwt) {
        String jti = jwt.getId();
        long exp = jwt.getExpiresAt().getEpochSecond();

        tokenBlacklistService.blacklistToken(jti, exp);

        return ResponseEntity.noContent().build();
    }
}