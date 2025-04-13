package com.newworld.saegil.authentication.service;

public record TokenRefreshResult(
        String accessToken,
        String refreshToken) {
}
