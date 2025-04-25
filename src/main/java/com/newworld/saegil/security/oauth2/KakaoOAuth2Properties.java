package com.newworld.saegil.security.oauth2;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "oauth2.kakao")
public record KakaoOAuth2Properties(
        String adminKey,
        String clientId,
        String clientSecret,
        String redirectUri,
        List<String> scope
) {
}
