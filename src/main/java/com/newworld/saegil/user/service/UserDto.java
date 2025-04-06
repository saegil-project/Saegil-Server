package com.newworld.saegil.user.service;

import com.newworld.saegil.user.domain.User;

public record UserDto(
        Long id,
        String name,
        String email,
        String profileImageUrl
) {

    public static UserDto from(final User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProfileImageUrl()
        );
    }
}
