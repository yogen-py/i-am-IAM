package org.gio.iam.security;

import org.gio.iam.service.TokenBlackListService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtRevocationFilter extends OncePerRequestFilter {

    private final TokenBlackListService blackListService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException,IOException{

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt){
            String jti  = jwt.getId();

            if (blackListService.isBlacklisted(jti)){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(" Token is revoked muchacho (Logged out)");
                return;
            }
        }

        filterChain.doFilter(request,response);
    }
}
