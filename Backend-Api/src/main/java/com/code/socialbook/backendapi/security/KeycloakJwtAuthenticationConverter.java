package com.code.socialbook.backendapi.security;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(@NotNull Jwt source) {
        return new JwtAuthenticationToken(
                source,
                Stream.concat(
                        new JwtGrantedAuthoritiesConverter().convert(source).stream(),
                        extractResourceRoles(source).stream())
                        .collect(Collectors.toSet())
        );
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(@NotNull Jwt source) {
        //For accessing roles within the issued token
        //First we access the resource access portion containing the userRoles
        var resource_access = new HashMap<>(source.getClaim("resource_access"));
        //Access the account object
        var userAccount =  (Map<String, List<String>>) resource_access.get("account");
        var roles = userAccount.get("roles");
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.replace("-", "_")))
                .collect(Collectors.toSet());
    }
}
