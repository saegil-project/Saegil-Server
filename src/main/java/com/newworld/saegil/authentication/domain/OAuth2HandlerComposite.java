package com.newworld.saegil.authentication.domain;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Component
public class OAuth2HandlerComposite {

    private final Map<OAuth2Type, OAuth2Handler> handlerMappings;

    public OAuth2HandlerComposite(final Set<OAuth2Handler> handlers) {
        this.handlerMappings = new EnumMap<>(OAuth2Type.class);
        for (final OAuth2Handler handler : handlers) {
            final OAuth2Type supportingOAuth2Type = handler.getSupportingOAuth2Type();
            handlerMappings.put(supportingOAuth2Type, handler);
        }
    }

    public OAuth2Handler findHandler(final String oauth2TypeName) {
        final OAuth2Type oauth2Type = OAuth2Type.from(oauth2TypeName);

        return findHandler(oauth2Type);
    }

    public OAuth2Handler findHandler(final OAuth2Type oauth2Type) {
        final OAuth2Handler handler = handlerMappings.get(oauth2Type);
        if (handler == null) {
            throw new UnsupportedOAuth2LoginException("지원하는 소셜 로그인 기능이 아닙니다.");
        }

        return handler;
    }
}
