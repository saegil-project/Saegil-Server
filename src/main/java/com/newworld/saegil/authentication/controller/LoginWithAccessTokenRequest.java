package com.newworld.saegil.authentication.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record LoginWithAccessTokenRequest(

        @NotEmpty(message = "Access Token을 입력해주세요.")
        @Schema(description = "OAuth 2.0 Access Token", example = "asdfasdfasdfasdf")
        String accessToken
) {
}
