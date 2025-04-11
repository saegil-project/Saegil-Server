package com.newworld.saegil.authentication.service;

import com.newworld.saegil.authentication.domain.OAuth2Handler;
import com.newworld.saegil.authentication.domain.OAuth2HandlerComposite;
import com.newworld.saegil.authentication.domain.OAuth2Type;
import com.newworld.saegil.authentication.domain.OAuth2UserInfo;
import com.newworld.saegil.authentication.domain.PrivateClaims;
import com.newworld.saegil.authentication.domain.Token;
import com.newworld.saegil.authentication.domain.TokenProcessor;
import com.newworld.saegil.user.domain.User;
import com.newworld.saegil.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final OAuth2HandlerComposite oauth2HandlerComposite;
    private final TokenProcessor tokenProcessor;
    private final UserRepository userRepository;

    public String getAuthCodeRequestUrl(final String oauth2TypeName) {
        final OAuth2Type oauth2Type = OAuth2Type.from(oauth2TypeName);
        final OAuth2Handler oauth2Handler = oauth2HandlerComposite.findHandler(oauth2Type);

        return oauth2Handler.provideAuthCodeRequestUrl();
    }

    public LoginResult login(
            final String oauth2TypeName,
            final String authorizationCode,
            final LocalDateTime requestTime
    ) {
        final OAuth2UserInfo oauth2UserInfo = fetchOAuth2UserInfo(oauth2TypeName, authorizationCode);
        final User user = findOrPersistUser(oauth2UserInfo);
        final PrivateClaims privateClaims = new PrivateClaims(user.getId());
        final Token token = tokenProcessor.generateToken(requestTime, privateClaims.toMap());

        return new LoginResult(user.getId(), token.accessToken(), token.refreshToken());
    }

    private OAuth2UserInfo fetchOAuth2UserInfo(final String oauth2TypeName, final String authorizationCode) {
        final OAuth2Type oauth2Type = OAuth2Type.from(oauth2TypeName);
        final OAuth2Handler oauth2Handler = oauth2HandlerComposite.findHandler(oauth2Type);
        final String oauth2AccessToken = oauth2Handler.getOAuth2AccessToken(authorizationCode);

        return oauth2Handler.getUserInfo(oauth2AccessToken);
    }

    private User findOrPersistUser(final OAuth2UserInfo oauth2UserInfo) {
        return userRepository.findByOauth2IdAndOauth2Type(
                oauth2UserInfo.oauth2Id(),
                oauth2UserInfo.oauth2Type()
        ).orElseGet(() -> {
            final User newUser = new User(
                    oauth2UserInfo.nickname(),
                    oauth2UserInfo.profileImageUrl(),
                    oauth2UserInfo.oauth2Id(),
                    oauth2UserInfo.oauth2Type()
            );

            return userRepository.save(newUser);
        });
    }
}
