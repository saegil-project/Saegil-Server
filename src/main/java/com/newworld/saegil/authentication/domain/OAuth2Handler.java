package com.newworld.saegil.authentication.domain;

public interface OAuth2Handler {

    OAuth2Type getSupportingOAuth2Type();
    String provideAuthCodeRequestUrl();
}
