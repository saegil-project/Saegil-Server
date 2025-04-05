package com.newworld.saegil.authentication.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record LogoutRequest(

        @NotEmpty(message = "서비스 Refresh Token을 입력해주세요.")
        @Schema(description = "서비스 Refresh Token", example = "Bearer refresh-token-opqrstuvwxyz")
        String refreshToken
) {
}
