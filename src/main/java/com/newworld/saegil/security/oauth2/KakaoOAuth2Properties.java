package com.newworld.saegil.security.oauth2;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth2.kakao")
public record KakaoOAuth2Properties(
        String clientId,
        String clientSecret,
        String redirectUri,
        List<String> scope
) {
}
