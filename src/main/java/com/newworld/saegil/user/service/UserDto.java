package com.newworld.saegil.user.service;

import com.newworld.saegil.authentication.domain.OAuth2Type;
import com.newworld.saegil.user.domain.User;

public record UserDto(
        Long id,
        String name,
        String profileImageUrl,
        String oauth2Id,
        OAuth2Type oauth2Type
) {

    public static UserDto from(final User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getProfileImageUrl(),
                user.getOauth2Id(),
                user.getOauth2Type()
        );
    }
}
