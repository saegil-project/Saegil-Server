package com.newworld.saegil.user.controller;

import com.newworld.saegil.user.service.UserDto;

public record ReadUserResponse(
        Long id,
        String name,
        String email,
        String profileImageUrl
) {

    public static ReadUserResponse from(final UserDto userDto) {
        return new ReadUserResponse(
                userDto.id(),
                userDto.name(),
                userDto.email(),
                userDto.profileImageUrl()
        );
    }
}
