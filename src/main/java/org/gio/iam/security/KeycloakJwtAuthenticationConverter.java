package org.gio.iam.security;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class KeycloakJwtAuthenticationConverter implements Converter<Jwt , AbstractAuthenticationToken>{

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt){
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        String principalClaimName = "sub";
        String principal =  jwt.getClaimAsString(principalClaimName);
        return new JwtAuthenticationToken(jwt,authorities,principal);

    }


    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt){
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        if (realmAccess == null || realmAccess.isEmpty()){
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        Collection<String> roles = (Collection<String>) realmAccess.get("roles");
        if (roles == null || roles.isEmpty()){
            return  Collections.emptyList();
        }

        return roles.stream()
                .map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());


    }
}
