package com.newworld.saegil.user.controller;

import com.newworld.saegil.user.service.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReadUserResponse(

        @Schema(description = "유저 식별자", example = "1")
        Long id,

        @Schema(description = "유저 이름", example = "김주민")
        String name,

        @Schema(description = "유저 프로필 이미지 url", example = "https://images.com/asdfasdf")
        String profileImage
) {

    public static ReadUserResponse from(final UserDto userDto) {
        return new ReadUserResponse(
                userDto.id(),
                userDto.name(),
                userDto.profileImageUrl()
        );
    }
}
