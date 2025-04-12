package com.newworld.saegil.authentication.domain;

public enum OAuth2Type {

    KAKAO,
    ;

    public static OAuth2Type from(final String typeName) {
        for (final OAuth2Type oauth2Type : values()) {
            if (oauth2Type.name().equalsIgnoreCase(typeName)) {
                return oauth2Type;
            }
        }

        throw new UnsupportedOAuth2LoginException(typeName + "은(는) 지원하는 OAuth2 타입이 아닙니다.");
    }
}
