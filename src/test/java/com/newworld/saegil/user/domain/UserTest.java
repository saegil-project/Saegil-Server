package com.newworld.saegil.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserTest {

    @Nested
    @DisplayName("User 생성 시")
    class Describe_createUser {

        private final String validEmail = "hong@example.com";
        private final String validProfileImageUrl = "https://example.com/profile.png";

        @Nested
        @DisplayName("정상적인 입력이면")
        class Context_with_valid_input {

            private final String validName = "홍길동";

            @Test
            void 예외가_발생하지_않는다() {
                assertThatCode(() -> new User(validName, validEmail, validProfileImageUrl))
                        .doesNotThrowAnyException();
            }
        }

        @Nested
        @DisplayName("이름이")
        class Context_with_invalid_name {

            private final String nullName = null;
            private final String emptyName = "";
            private final String blankName = "   ";
            private final String longName = "가".repeat(21);

            @Test
            void null이면_예외가_발생한다() {
                assertThatThrownBy(() -> new User(nullName, validEmail, validProfileImageUrl))
                        .isInstanceOf(InvalidUserException.class)
                        .hasMessageContaining("이름은 필수입니다.");
            }

            @Test
            void 비어있으면_예외가_발생한다() {
                assertThatThrownBy(() -> new User(emptyName, validEmail, validProfileImageUrl))
                        .isInstanceOf(InvalidUserException.class)
                        .hasMessageContaining("이름은 필수입니다.");
            }

            @Test
            void 공백이면_예외가_발생한다() {
                assertThatThrownBy(() -> new User(blankName, validEmail, validProfileImageUrl))
                        .isInstanceOf(InvalidUserException.class)
                        .hasMessageContaining("이름은 필수입니다.");
            }

            @Test
            void 너무_길면_예외가_발생한다() {
                assertThatThrownBy(() -> new User(longName, validEmail, validProfileImageUrl))
                        .isInstanceOf(InvalidUserException.class)
                        .hasMessageContaining("이름은 최대 20자까지 가능합니다.");
            }
        }
    }
}
