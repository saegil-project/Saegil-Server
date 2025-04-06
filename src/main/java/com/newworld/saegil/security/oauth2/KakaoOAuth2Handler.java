package com.newworld.saegil.security.oauth2;

import com.newworld.saegil.authentication.domain.OAuth2Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoOAuth2Handler implements OAuth2Handler {

    private final KakaoOAuth2Properties kakaoOAuth2Properties;

    @Override
    public String provideAuthCodeRequestUrl() {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", kakaoOAuth2Properties.clientId())
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", kakaoOAuth2Properties.redirectUri())
                .queryParam("scope", String.join(",", kakaoOAuth2Properties.scope()))
                .toUriString();
    }
}
