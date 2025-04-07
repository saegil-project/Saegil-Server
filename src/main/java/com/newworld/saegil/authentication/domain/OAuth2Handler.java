package com.newworld.saegil.authentication.domain;

public interface OAuth2Handler {

    OAuth2Type getSupportingOAuth2Type();
    String provideAuthCodeRequestUrl();
    String getOAuth2AccessToken(final String authorizationCode);
    OAuth2UserInfo getUserInfo(final String accessToken);
}
