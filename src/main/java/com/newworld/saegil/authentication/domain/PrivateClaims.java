package com.newworld.saegil.authentication.domain;

import java.util.Map;

import io.jsonwebtoken.Claims;

public record PrivateClaims(Long userId) {

    private static final String USER_ID_KEY_NAME = "userId";

    public static PrivateClaims from(final Claims claims) {
        final Long userId = claims.get(USER_ID_KEY_NAME, Long.class);

        return new PrivateClaims(userId);
    }

    public Map<String, Object> toMap() {
        return Map.of(
                USER_ID_KEY_NAME, userId
        );
    }
}
