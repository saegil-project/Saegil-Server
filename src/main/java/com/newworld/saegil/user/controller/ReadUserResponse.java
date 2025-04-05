package com.newworld.saegil.user.controller;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReadUserResponse(

        @Schema(description = "유저 식별자", example = "1")
        Long id,

        @Schema(description = "유저 이름", example = "김주민")
        String name,

        @Schema(description = "유저 이메일", example = "neighbor_kim@naver.com")
        String email,

        @Schema(description = "유저 프로필 이미지 url", example = "https://images.com/asdfasdf")
        String profileImage
) {
}
