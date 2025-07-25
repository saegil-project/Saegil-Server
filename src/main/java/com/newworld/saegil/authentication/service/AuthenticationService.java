package com.newworld.saegil.authentication.service;

import com.newworld.saegil.authentication.domain.BlacklistToken;
import com.newworld.saegil.authentication.domain.InvalidTokenException;
import com.newworld.saegil.authentication.domain.OAuth2Handler;
import com.newworld.saegil.authentication.domain.OAuth2HandlerComposite;
import com.newworld.saegil.authentication.domain.OAuth2UserInfo;
import com.newworld.saegil.authentication.domain.PrivateClaims;
import com.newworld.saegil.authentication.domain.Token;
import com.newworld.saegil.authentication.domain.TokenProcessor;
import com.newworld.saegil.authentication.domain.TokenType;
import com.newworld.saegil.authentication.repository.BlacklistTokenRepository;
import com.newworld.saegil.exception.UserNotFoundException;
import com.newworld.saegil.user.domain.User;
import com.newworld.saegil.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final BlacklistTokenRepository blacklistTokenRepository;

    @Value("${user.default.profile-image-url}")
    private String defaultProfileImageUrl;

    public String getAuthCodeRequestUrl(final String oauth2TypeName) {
        final OAuth2Handler oauth2Handler = oauth2HandlerComposite.findHandler(oauth2TypeName);

        return oauth2Handler.provideAuthCodeRequestUrl();
    }

    public LoginResult loginWithAuthorizationCode(
            final String oauth2TypeName,
            final String authorizationCode,
            final LocalDateTime requestTime
    ) {
        final OAuth2Handler oauth2Handler = oauth2HandlerComposite.findHandler(oauth2TypeName);
        final String oauth2AccessToken = oauth2Handler.getOAuth2AccessToken(authorizationCode);
        final OAuth2UserInfo oauth2UserInfo = oauth2Handler.getUserInfo(oauth2AccessToken);
        final User user = findOrPersistUser(oauth2UserInfo);
        final PrivateClaims privateClaims = new PrivateClaims(user.getId());
        final Token token = tokenProcessor.generateToken(requestTime, privateClaims.toMap());

        return new LoginResult(user.getId(), token.accessToken(), token.refreshToken());
    }

    private User findOrPersistUser(final OAuth2UserInfo oauth2UserInfo) {
        return userRepository.findByOauth2IdAndOauth2Type(
                oauth2UserInfo.oauth2Id(),
                oauth2UserInfo.oauth2Type()
        ).orElseGet(() -> {
            final User newUser = new User(
                    oauth2UserInfo.nickname(),
                    defaultProfileImageUrl,
                    oauth2UserInfo.oauth2Id(),
                    oauth2UserInfo.oauth2Type()
            );

            return userRepository.save(newUser);
        });
    }

    public LoginResult loginWithAccessToken(
            final String oauth2TypeName,
            final String oauth2AccessToken,
            final String deviceToken,
            final LocalDateTime requestTime
    ) {
        final OAuth2Handler oauth2Handler = oauth2HandlerComposite.findHandler(oauth2TypeName);
        final OAuth2UserInfo oauth2UserInfo = oauth2Handler.getUserInfo(oauth2AccessToken);
        final User user = findOrPersistUser(oauth2UserInfo);
        user.upsertDeviceToken(deviceToken);
        userRepository.save(user);

        final PrivateClaims privateClaims = new PrivateClaims(user.getId());
        final Token token = tokenProcessor.generateToken(requestTime, privateClaims.toMap());

        return new LoginResult(user.getId(), token.accessToken(), token.refreshToken());
    }

    public PrivateClaims getValidPrivateClaims(final TokenType tokenType, final String token) {
        final Claims claims = tokenProcessor.decode(tokenType, token)
                                            .orElseThrow(() -> new InvalidTokenException("유효한 토큰이 아닙니다."));
        final PrivateClaims privateClaims = PrivateClaims.from(claims);

        final Long userId = privateClaims.userId();
        if (!userRepository.existsById(userId)) {
            throw new NoSuchUserException("존재하지 않는 유저의 토큰입니다.");
        }
        if (blacklistTokenRepository.existsByUserIdAndToken(userId, token)) {
            throw new InvalidTokenException("사용할 수 없는 토큰입니다.");
        }

        return privateClaims;
    }

    public void logout(final String accessToken, final String refreshToken) {
        final PrivateClaims privateClaims = getValidPrivateClaims(TokenType.ACCESS, accessToken);

        final User user = userRepository.findById(privateClaims.userId())
                                        .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
        user.invalidateDeviceToken();

        final BlacklistToken blacklistAccessToken = new BlacklistToken(
                privateClaims.userId(),
                TokenType.ACCESS,
                accessToken
        );
        final BlacklistToken blacklistRefreshToken = new BlacklistToken(
                privateClaims.userId(),
                TokenType.REFRESH,
                refreshToken
        );

        userRepository.save(user);
        blacklistTokenRepository.save(blacklistAccessToken);
        blacklistTokenRepository.save(blacklistRefreshToken);
    }

    public boolean isValidToken(final TokenType tokenType, final String targetToken) {
        final PrivateClaims privateClaims = tokenProcessor.decode(tokenType, targetToken)
                                                          .map(PrivateClaims::from)
                                                          .orElse(null);
        if (privateClaims == null) {
            return false;
        }

        return userRepository.existsById(privateClaims.userId()) &&
                !blacklistTokenRepository.existsByUserIdAndToken(privateClaims.userId(), targetToken);
    }

    public TokenRefreshResult refreshToken(final LocalDateTime requestTime, final String refreshToken) {
        final PrivateClaims privateClaims = getValidPrivateClaims(TokenType.REFRESH, refreshToken);
        final Token token = tokenProcessor.generateToken(requestTime, privateClaims.toMap());

        final BlacklistToken blacklistRefreshToken = new BlacklistToken(
                privateClaims.userId(),
                TokenType.REFRESH,
                refreshToken
        );
        blacklistTokenRepository.save(blacklistRefreshToken);

        return new TokenRefreshResult(token.accessToken(), token.refreshToken());
    }

    public void withdrawal(final String accessToken, final String refreshToken) {
        final PrivateClaims privateClaims = getValidPrivateClaims(TokenType.ACCESS, accessToken);
        final User user = userRepository.findById(privateClaims.userId())
                                        .orElseThrow(() -> new InvalidWithdrawalException("이미 탈퇴되었습니다."));
        final OAuth2Handler handler = oauth2HandlerComposite.findHandler(user.getOauth2Type());

        blacklistTokenRepository.save(new BlacklistToken(user.getId(), TokenType.ACCESS, accessToken));
        blacklistTokenRepository.save(new BlacklistToken(user.getId(), TokenType.REFRESH, refreshToken));
        userRepository.deleteById(user.getId());
        handler.unlink(user.getOauth2Id());
    }
}
