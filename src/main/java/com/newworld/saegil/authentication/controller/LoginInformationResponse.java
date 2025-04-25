package com.newworld.saegil.authentication.controller;

import com.newworld.saegil.authentication.service.LoginResult;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginInformationResponse(

        @Schema(description = "서비스 Access Token", example = "Bearer access-token-abcdefghijklmn")
        String accessToken,

        @Schema(description = "서비스 Refresh Token", example = "Bearer refresh-token-opqrstuvwxyz")
        String refreshToken
) {

    public static LoginInformationResponse from(final LoginResult loginResult) {
        return new LoginInformationResponse(
                loginResult.accessToken(),
                loginResult.refreshToken()
        );
    }
}
