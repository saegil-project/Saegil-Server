package com.newworld.saegil.authentication.controller;

import io.swagger.v3.oas.annotations.media.Schema;

public record ValidateTokenResponse(

        @Schema(description = "서비스 Access Token 유효성 검증 결과", example = "true")
        boolean validated
) {
}
