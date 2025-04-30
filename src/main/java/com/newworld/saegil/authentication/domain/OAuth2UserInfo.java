package com.newworld.saegil.authentication.domain;

public record OAuth2UserInfo(
        String oauth2Id,
        OAuth2Type oauth2Type,
        String nickname
) {
}
