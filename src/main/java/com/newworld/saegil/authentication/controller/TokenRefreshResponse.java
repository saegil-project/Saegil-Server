package com.newworld.saegil.authentication.controller;

import com.newworld.saegil.authentication.service.TokenRefreshResult;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenRefreshResponse(

        @Schema(description = "서비스 Access Token", example = "Bearer access-token-abcdefghijklmn")
        String accessToken,

        @Schema(description = "서비스 Refresh Token", example = "Bearer refresh-token-opqrstuvwxyz")
        String refreshToken
) {

    public static TokenRefreshResponse from(final TokenRefreshResult result) {
        return new TokenRefreshResponse(result.accessToken(), result.refreshToken());
    }
}
