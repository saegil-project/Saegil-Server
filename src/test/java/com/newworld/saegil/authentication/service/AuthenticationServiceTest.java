package com.newworld.saegil.authentication.service;

import com.newworld.saegil.authentication.domain.OAuth2Handler;
import com.newworld.saegil.authentication.domain.OAuth2HandlerComposite;
import com.newworld.saegil.authentication.domain.OAuth2Type;
import com.newworld.saegil.authentication.domain.OAuth2UserInfo;
import com.newworld.saegil.user.domain.User;
import com.newworld.saegil.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

                // when
                final LoginResult result = authenticationService.login("KAKAO", 카카오_인증_코드, LocalDateTime.now());
                entityManager.flush();
                entityManager.clear();

                // then
                final User savedUser = userRepository.findById(1L).get();

                SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(result.id()).isEqualTo(savedUser.getId());
                    softly.assertThat(result.accessToken()).startsWith(SERVICE_TOKEN_PREFIX);
                    softly.assertThat(result.refreshToken()).startsWith(SERVICE_TOKEN_PREFIX);
                });
            }
        }
    }
}
