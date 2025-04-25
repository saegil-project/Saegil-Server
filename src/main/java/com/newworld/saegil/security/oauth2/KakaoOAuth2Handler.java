package com.newworld.saegil.security.oauth2;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.newworld.saegil.authentication.domain.OAuth2Handler;
import com.newworld.saegil.authentication.domain.OAuth2Type;
import com.newworld.saegil.authentication.domain.OAuth2UserInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoOAuth2Handler implements OAuth2Handler {

    private final KakaoOAuth2Properties kakaoOAuth2Properties;
    private final RestTemplate restTemplate;

    @Override
    public OAuth2Type getSupportingOAuth2Type() {
        return OAuth2Type.KAKAO;
    }

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

    @Override
    public String getOAuth2AccessToken(final String authorizationCode) {
        final HttpEntity<MultiValueMap<String, String>> requestEntity = createAccessTokenRequest(authorizationCode);

        final ResponseEntity<Map> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new OAuth2ProcessingException("Kakao Access Token 요청에 실패했습니다.");
        }

        return response.getBody().get("access_token").toString();
    }

    private HttpEntity<MultiValueMap<String, String>> createAccessTokenRequest(final String authorizationCode) {
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        final MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", kakaoOAuth2Properties.clientId());
        requestBody.add("redirect_uri", kakaoOAuth2Properties.redirectUri());
        requestBody.add("code", authorizationCode);
        requestBody.add("client_secret", kakaoOAuth2Properties.clientSecret());

        return new HttpEntity<>(requestBody, requestHeaders);
    }

    @Override
    public OAuth2UserInfo getUserInfo(final String accessToken) {
        final HttpEntity<Void> requestEntity = createUserInfoRequest(accessToken);
        final ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                requestEntity,
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new OAuth2ProcessingException("Kakao 사용자 정보 요청에 실패했습니다.");
        }

        return parseOAuth2UserInfo(response);
    }

    private HttpEntity<Void> createUserInfoRequest(final String accessToken) {
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestHeaders.setBearerAuth(accessToken);

        return new HttpEntity<>(requestHeaders);
    }

    private OAuth2UserInfo parseOAuth2UserInfo(final ResponseEntity<Map> response) {
        final Map<String, Object> body = response.getBody();
        final String id = body.get("id").toString();

        final Map<String, Object> properties = (Map<String, Object>)body.get("properties");
        final String nickname = (String)properties.get("nickname");
        final String profileImageUrl = (String)properties.get("profile_image");

        return new OAuth2UserInfo(id, OAuth2Type.KAKAO, nickname, profileImageUrl);
    }
}
