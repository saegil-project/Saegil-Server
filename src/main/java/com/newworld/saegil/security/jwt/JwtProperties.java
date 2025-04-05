package com.newworld.saegil.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("token.jwt")
public record JwtProperties(
        String accessKey,
        String refreshKey,
        long accessExpiredHours,
        long refreshExpiredHours
) {
}
