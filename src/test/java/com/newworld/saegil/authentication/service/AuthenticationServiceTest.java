package com.newworld.saegil.authentication.service;

import com.newworld.saegil.authentication.domain.*;
import com.newworld.saegil.authentication.repository.BlacklistTokenRepository;
import com.newworld.saegil.user.domain.User;
import com.newworld.saegil.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlacklistTokenRepository blacklistTokenRepository;

    @Autowired
    private TokenProcessor tokenProcessor;

    @MockitoBean
    private OAuth2HandlerComposite oauth2HandlerComposite;

    @MockitoBean
    private OAuth2Handler mockOAuth2Handler;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        given(oauth2HandlerComposite.findHandler(any())).willReturn(mockOAuth2Handler);
    }

    @Nested
    @DisplayName("로그인 할 때")
    class Describe_login {

        private static final String 카카오_인증_코드 = "authorization code";
        private static final String 카카오_액세스_토큰 = "oauth2-access-token";
        private static final String SERVICE_TOKEN_PREFIX = "Bearer ";
        private static final OAuth2UserInfo OAUTH2_회원_정보 = new OAuth2UserInfo(
                "1234567",
                OAuth2Type.KAKAO,
                "홍길동",
                "http://example.com/profile.jpg"
        );

        @Nested
        @DisplayName("가입된 사용자가 로그인 한 것이면")
        class Context_with_existing_user {

            @Test
            void 바로_access_token과_refresh_token을_발급한다() {
                // given
                final User 가입된_유저 = userRepository.save(
                        new User(
                                OAUTH2_회원_정보.nickname(),
                                OAUTH2_회원_정보.profileImageUrl(),
                                OAUTH2_회원_정보.oauth2Id(),
                                OAUTH2_회원_정보.oauth2Type()
                        )
                );
                entityManager.flush();
                entityManager.clear();

                given(mockOAuth2Handler.getOAuth2AccessToken(카카오_인증_코드)).willReturn(카카오_액세스_토큰);
                given(mockOAuth2Handler.getUserInfo(카카오_액세스_토큰)).willReturn(OAUTH2_회원_정보);

                // when
                final LoginResult result = authenticationService.login("KAKAO", 카카오_인증_코드, LocalDateTime.now());

                // then
                SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(result.id()).isEqualTo(가입된_유저.getId());
                    softly.assertThat(result.accessToken()).startsWith(SERVICE_TOKEN_PREFIX);
                    softly.assertThat(result.refreshToken()).startsWith(SERVICE_TOKEN_PREFIX);
                });
            }
        }

        @Nested
        @DisplayName("최초로_로그인한_것이면")
        class Context_with_new_user {

            @Test
            void 회원가입_후_토큰을_발급하고_정상_응답한다() {
                // given
                given(mockOAuth2Handler.getOAuth2AccessToken(카카오_인증_코드)).willReturn(카카오_액세스_토큰);
                given(mockOAuth2Handler.getUserInfo(카카오_액세스_토큰)).willReturn(OAUTH2_회원_정보);
                final long userCountBeforeSignup = userRepository.count();

                // when
                final LoginResult result = authenticationService.login("KAKAO", 카카오_인증_코드, LocalDateTime.now());
                entityManager.flush();
                entityManager.clear();

                // then
                final long userCountAfterSignup = userRepository.count();

                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(userCountAfterSignup > userCountBeforeSignup).isTrue();
                    softAssertions.assertThat(result.accessToken()).startsWith(SERVICE_TOKEN_PREFIX);
                    softAssertions.assertThat(result.refreshToken()).startsWith(SERVICE_TOKEN_PREFIX);
                });
            }
        }
    }

    @Nested
    @DisplayName("유효한 PrivateClaims를 가져올 때")
    class Describe_get_valid_private_claims {

        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_valid_token {

            @Test
            void PrivateClaims를_반환한다() {
                // given
                final User user = new User("권예진", "http://example.com/profile.jpg", "1234567", OAuth2Type.KAKAO);
                userRepository.save(user);
                final Token token = tokenProcessor.generateToken(LocalDateTime.now(), new PrivateClaims(user.getId()).toMap());

                // when
                final PrivateClaims privateClaims = authenticationService.getValidPrivateClaims(TokenType.ACCESS, token.accessToken());

                // then
                assertThat(privateClaims.userId()).isEqualTo(user.getId());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰 즉")
        class Context_with_invalid_token {

            @Test
            void 존재하지_않는_유저의_토큰이면_예외가_발생한다() {
                // given
                final Token token = tokenProcessor.generateToken(LocalDateTime.now(), new PrivateClaims(-999L).toMap());

                // when & then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThatThrownBy(
                                    () -> authenticationService.getValidPrivateClaims(TokenType.ACCESS, token.accessToken()))
                            .isInstanceOf(NoSuchUserException.class)
                            .hasMessage("존재하지 않는 유저의 토큰입니다.");

                    softAssertions.assertThatThrownBy(
                                    () -> authenticationService.getValidPrivateClaims(TokenType.REFRESH, token.refreshToken()))
                            .isInstanceOf(NoSuchUserException.class)
                            .hasMessage("존재하지 않는 유저의 토큰입니다.");
                });
            }

            @Test
            void 로그아웃된_토큰이면_예외가_발생한다() {
                // given
                final User user = new User("권예진", "http://example.com/profile.jpg", "1234567", OAuth2Type.KAKAO);
                userRepository.save(user);
                final Token token = tokenProcessor.generateToken(LocalDateTime.now(),
                        new PrivateClaims(user.getId()).toMap());
                blacklistTokenRepository.save(new BlacklistToken(user.getId(), TokenType.ACCESS, token.accessToken()));
                blacklistTokenRepository.save(
                        new BlacklistToken(user.getId(), TokenType.REFRESH, token.refreshToken()));

                entityManager.flush();
                entityManager.clear();

                // when & then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThatThrownBy(
                                    () -> authenticationService.getValidPrivateClaims(TokenType.ACCESS, token.accessToken()))
                            .isInstanceOf(InvalidTokenException.class)
                            .hasMessage("사용할 수 없는 토큰입니다.");
                    softAssertions.assertThatThrownBy(
                                    () -> authenticationService.getValidPrivateClaims(TokenType.REFRESH, token.refreshToken()))
                            .isInstanceOf(InvalidTokenException.class)
                            .hasMessage("사용할 수 없는 토큰입니다.");
                });
            }
        }
    }

    @Nested
    @DisplayName("로그아웃 할 때")
    class Describe_logout {

        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_valid_token {

            @Test
            void 토큰이_블랙리스트_토큰_목록에_등록된다() {
                // given
                final User user = new User("권예진", "http://example.com/profile.jpg", "1234567", OAuth2Type.KAKAO);
                userRepository.save(user);
                final Token token = tokenProcessor.generateToken(LocalDateTime.now(),
                        new PrivateClaims(user.getId()).toMap());

                // when
                authenticationService.logout(token.accessToken(), token.refreshToken());
                entityManager.flush();
                entityManager.clear();

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(
                                    blacklistTokenRepository.existsByUserIdAndToken(user.getId(), token.accessToken()))
                            .isTrue();
                    softAssertions.assertThat(
                                    blacklistTokenRepository.existsByUserIdAndToken(user.getId(), token.refreshToken()))
                            .isTrue();
                });
            }

            @Test
            void 로그아웃에_사용된_토큰은_더이상_유효하지_않다() {
                // given
                final User user = new User("권예진", "http://example.com/profile.jpg", "1234567", OAuth2Type.KAKAO);
                userRepository.save(user);
                final Token token = tokenProcessor.generateToken(LocalDateTime.now(),
                        new PrivateClaims(user.getId()).toMap());

                // when
                authenticationService.logout(token.accessToken(), token.refreshToken());
                entityManager.flush();
                entityManager.clear();

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(authenticationService.isValidToken(TokenType.ACCESS, token.accessToken()))
                            .isFalse();
                    softAssertions.assertThat(
                            authenticationService.isValidToken(TokenType.REFRESH, token.refreshToken())).isFalse();
                });
            }
        }
    }

    @Nested
    @DisplayName("토큰 유효성 검사 시")
    class Describe_validate_token {

        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_valid_token {

            @Test
            void true를_반환한다() {
                // given
                final User user = new User("권예진", "http://example.com/profile.jpg", "1234567", OAuth2Type.KAKAO);
                userRepository.save(user);
                final Token token = tokenProcessor.generateToken(LocalDateTime.now(),
                        new PrivateClaims(user.getId()).toMap());
                entityManager.flush();
                entityManager.clear();

                // when
                final boolean actual = authenticationService.isValidToken(TokenType.ACCESS, token.accessToken());

                // then
                assertThat(actual).isTrue();
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰 즉")
        class Context_with_invalid_token {

            @Test
            void 존재하지_않는_유저의_토큰이면_false를_반환한다() {
                // given
                final Token token = tokenProcessor.generateToken(LocalDateTime.now(), new PrivateClaims(-999L).toMap());

                // when
                final boolean actual = authenticationService.isValidToken(TokenType.ACCESS, token.accessToken());

                // then
                assertThat(actual).isFalse();
            }

            @Test
            void 로그아웃된_토큰이면_false를_반환한다() {
                // given
                final User user = new User("권예진", "http://example.com/profile.jpg", "1234567", OAuth2Type.KAKAO);
                userRepository.save(user);
                final Token token = tokenProcessor.generateToken(LocalDateTime.now(),
                        new PrivateClaims(user.getId()).toMap());
                blacklistTokenRepository.save(new BlacklistToken(user.getId(), TokenType.ACCESS, token.accessToken()));
                blacklistTokenRepository.save(
                        new BlacklistToken(user.getId(), TokenType.REFRESH, token.refreshToken()));

                entityManager.flush();
                entityManager.clear();

                // when
                final boolean actual = authenticationService.isValidToken(TokenType.ACCESS, token.accessToken());

                // then
                assertThat(actual).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("리프레시 토큰으로 토큰을 갱신할 때")
    class Describe_refreshToken {

        @Nested
        @DisplayName("유효한 리프레시 토큰이 주어지면")
        class Context_with_valid_refresh_token {

            @Test
            void 토큰_갱신에_사용한_refreshToken은_블랙리스트에_등록된다() {
                // given
                final User user = new User("권예진", "http://example.com/profile.jpg", "1234567", OAuth2Type.KAKAO);
                userRepository.save(user);
                final Token token = tokenProcessor.generateToken(LocalDateTime.now(),
                        new PrivateClaims(user.getId()).toMap());
                entityManager.flush();
                entityManager.clear();

                // when
                authenticationService.refreshToken(LocalDateTime.now(), token.refreshToken());
                entityManager.flush();
                entityManager.clear();

                // then
                final boolean isBlacklisted =
                        blacklistTokenRepository.existsByUserIdAndToken(user.getId(), token.refreshToken());

                assertThat(isBlacklisted).isTrue();
            }

            @Test
            void 새로운_액세스_토큰과_리프레시_토큰을_발급한다() {
                // given
                final User user = new User("권예진", "http://example.com/profile.jpg", "1234567", OAuth2Type.KAKAO);
                userRepository.save(user);
                final LocalDateTime 갱신요청시간 = LocalDateTime.now();
                final LocalDateTime 갱신요청_10분전 = 갱신요청시간.minusMinutes(10L);
                final Token token = tokenProcessor.generateToken(갱신요청_10분전, new PrivateClaims(user.getId()).toMap());
                entityManager.flush();
                entityManager.clear();

                // when
                final TokenRefreshResult actual = authenticationService.refreshToken(갱신요청시간, token.refreshToken());

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual.accessToken()).startsWith("Bearer ");
                    softAssertions.assertThat(actual.accessToken()).isNotEqualTo(token.accessToken());
                    softAssertions.assertThat(actual.refreshToken()).startsWith("Bearer ");
                    softAssertions.assertThat(actual.refreshToken()).isNotEqualTo(token.refreshToken());
                });
            }
        }
    }
}
