package com.newworld.saegil.authentication.domain;

import com.newworld.saegil.security.oauth2.KakaoOAuth2Handler;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OAuth2HandlerCompositeTest {

    @Nested
    @DisplayName("지원하는 OAuth2 핸들러를 찾는 메서드에")
    class Describe_findHandler {

        @Nested
        @DisplayName("지원하는 OAuth2 타입을 전달하면")
        class Context_valid_oauth2_type {

            @Test
            void 해당_OAuth2_핸들러를_반환한다() {
                // given
                final OAuth2Handler kakaoHandler = new KakaoOAuth2Handler(null, null);
                final OAuth2HandlerComposite composite = new OAuth2HandlerComposite(Set.of(kakaoHandler));

                // when
                final OAuth2Handler actual = composite.findHandler(OAuth2Type.KAKAO);

                // then
                assertThat(actual).isInstanceOf(KakaoOAuth2Handler.class);
            }
        }

        @Nested
        @DisplayName("지원하지 않는 OAuth2 타입을 전달하면")
        class Context_invalid_oauth2_type {

            @Test
            void 예외가_발생한다() {
                // given
                final OAuth2HandlerComposite emptyComposite = new OAuth2HandlerComposite(Set.of());

                // when and then
                assertThatThrownBy(() -> emptyComposite.findHandler(OAuth2Type.KAKAO))
                        .isInstanceOf(UnsupportedOAuth2LoginException.class)
                        .hasMessage("지원하는 소셜 로그인 기능이 아닙니다.");
            }
        }
    }
}
