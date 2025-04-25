package com.newworld.saegil.authentication.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OAuth2TypeTest {

    @Nested
    @DisplayName("문자열에 해당하는 OAuth2Type을 찾을 때")
    class Describe_from {

        @Nested
        @DisplayName("유효한 타입 이름이 주어지면")
        class Context_valid_type_name {

            @Test
            void 대소문자_구분없이_적절한_OAuth2Type을_반환한다() {
                // given
                final OAuth2Type expect = OAuth2Type.KAKAO;
                String upperCaseTypeName = expect.name().toUpperCase();
                String lowerCaseTypeName = expect.name().toLowerCase();

                // when & then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThatCode(() -> OAuth2Type.from(upperCaseTypeName))
                            .doesNotThrowAnyException();

                    softAssertions.assertThat(OAuth2Type.from(upperCaseTypeName))
                            .isEqualTo(expect);

                    softAssertions.assertThatCode(() -> OAuth2Type.from(lowerCaseTypeName))
                            .doesNotThrowAnyException();

                    softAssertions.assertThat(OAuth2Type.from(lowerCaseTypeName))
                            .isEqualTo(expect);
                });
            }
        }

        @Nested
        @DisplayName("유효하지 않은 타입 이름이 주어지면")
        class Context_invalid_type_name {

            @Test
            void 예외가_발생한다() {
                // given
                String invalidType = "INVALID";

                // when & then
                assertThatThrownBy(() -> OAuth2Type.from(invalidType))
                        .isInstanceOf(UnsupportedOAuth2LoginException.class)
                        .hasMessage("INVALID은(는) 지원하는 OAuth2 타입이 아닙니다.");
            }
        }
    }
}
