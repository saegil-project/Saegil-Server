package com.newworld.saegil.authentication.service;

import com.newworld.saegil.authentication.domain.OAuth2Handler;
import com.newworld.saegil.authentication.domain.OAuth2HandlerComposite;
import com.newworld.saegil.authentication.domain.OAuth2Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final OAuth2HandlerComposite oauth2HandlerComposite;

    public String getAuthCodeRequestUrl(final String oauth2TypeName) {
        final OAuth2Type oauth2Type = OAuth2Type.from(oauth2TypeName);
        final OAuth2Handler oauth2Handler = oauth2HandlerComposite.findHandler(oauth2Type);

        return oauth2Handler.provideAuthCodeRequestUrl();
    }
}
