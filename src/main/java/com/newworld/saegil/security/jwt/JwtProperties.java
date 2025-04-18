package com.newworld.saegil.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.newworld.saegil.authentication.domain.TokenType;

@ConfigurationProperties("token.jwt")
public record JwtProperties(
        String accessKey,
        String refreshKey,
        long accessExpiredHours,
        long refreshExpiredHours
) {

    public String findTokenKey(final TokenType tokenType) {
        if (TokenType.ACCESS == tokenType) {
            return accessKey;
        }

        return refreshKey;
    }

    public Long findExpiredHours(final TokenType tokenType) {
        if (TokenType.ACCESS == tokenType) {
            return accessExpiredHours;
        }

        return refreshExpiredHours;
    }
}
