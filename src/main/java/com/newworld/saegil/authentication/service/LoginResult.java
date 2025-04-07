package com.newworld.saegil.authentication.service;

public record LoginResult(
        Long id,
        String accessToken,
        String refreshToken
) {
}
