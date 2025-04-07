package com.newworld.saegil.security.jwt;

import com.newworld.saegil.authentication.domain.InvalidTokenException;
import com.newworld.saegil.authentication.domain.TokenType;
import io.jsonwebtoken.Claims;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JwtTokenProcessorTest {

    private final JwtProperties jwtProperties = new JwtProperties(
            "ThisIsLocalAccessKeyThisIsLocalAccessKey",
            "ThisIsLocalRefreshKeyThisIsLocalRefreshKey",
            1L,
            336L
    );

    @Nested
    @DisplayName("encode 메서드는")
    class Describe_encode {

        @Test
        void Bearer_접두사가_포함된_토큰을_반환한다() {
            // given
            JwtTokenProcessor jwtProcessor = new JwtTokenProcessor(jwtProperties);
            String keyName = "userId";
            Long userId = 123L;
            Map<String, Object> claims = Map.of(keyName, userId);

            // when
            String actual = jwtProcessor.encode(
                    LocalDateTime.now(ZoneId.of("Asia/Seoul")),
                    TokenType.ACCESS,
                    claims
            );

            // then
            assertThat(actual).startsWith("Bearer ");
        }
    }

    @Nested
    @DisplayName("decode 메서드는")
    class Describe_decode {

        @Nested
        @DisplayName("토큰이 유효하면")
        class Context_valid_token {

            @Test
            void Claims_정보를_반환한다() {
                // given
                final JwtTokenProcessor jwtProcessor = new JwtTokenProcessor(jwtProperties);
                final String keyName = "userId";
                final Long userId = 123L;
                final TokenType tokenType = TokenType.ACCESS;
                final Map<String, Object> claims = Map.of(keyName, userId);
                final String token = jwtProcessor.encode(
                        LocalDateTime.now(ZoneId.of("Asia/Seoul")),
                        tokenType,
                        claims
                );

                // when
                final Optional<Claims> actual = jwtProcessor.decode(tokenType, token);

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).isPresent();
                    softAssertions.assertThat(actual.get().get(keyName, Long.class)).isEqualTo(userId);
                });
            }
        }

        @Nested
        @DisplayName("토큰이")
        class Context_invalid_token {

            @Test
            void null이면_예외가_발생한다() {
                // given
                final JwtTokenProcessor jwtProcessor = new JwtTokenProcessor(jwtProperties);
                final String nullToken = null;

                // when & then
                assertThatThrownBy(() -> jwtProcessor.decode(TokenType.ACCESS, nullToken))
                        .isInstanceOf(InvalidTokenException.class)
                        .hasMessage("토큰이 비어있습니다.");
            }

            @Test
            void 빈_문자열이면_예외가_발생한다() {
                // given
                final JwtTokenProcessor jwtProcessor = new JwtTokenProcessor(jwtProperties);
                final String blankToken = "   ";

                // when & then
                assertThatThrownBy(() -> jwtProcessor.decode(TokenType.ACCESS, blankToken))
                        .isInstanceOf(InvalidTokenException.class)
                        .hasMessage("토큰이 비어있습니다.");
            }

            @Test
            void Bearer_접두사로_시작하지_않으면_예외가_발생한다() {
                // given
                final JwtTokenProcessor jwtProcessor = new JwtTokenProcessor(jwtProperties);
                final String invalidToken = "Basic abc.def.ghi";

                // when & then
                assertThatThrownBy(() -> jwtProcessor.decode(TokenType.ACCESS, invalidToken))
                        .isInstanceOf(InvalidTokenException.class)
                        .hasMessage("Bearer 타입이 아닙니다.");
            }
        }

        @Nested
        @DisplayName("JWT 파싱에")
        class Context_parse_jwt {

            @Test
            void 성공하면_디코딩한_값을_반환한다() {
                // given
                final JwtTokenProcessor jwtProcessor = new JwtTokenProcessor(jwtProperties);
                final String keyName = "userId";
                final Long userId = 123L;
                final Map<String, Object> claims = Map.of(keyName, userId);
                final String encodedToken = jwtProcessor.encode(
                        LocalDateTime.now(ZoneId.of("Asia/Seoul")),
                        TokenType.ACCESS,
                        claims
                );

                // when
                final Optional<Claims> actual = jwtProcessor.decode(TokenType.ACCESS, encodedToken);

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).isPresent();
                    softAssertions.assertThat(actual.get().get(keyName, Long.class)).isEqualTo(userId);
                });
            }

            @Test
            void 실패하면_빈_Optional을_반환한다() {
                // given
                final JwtTokenProcessor jwtProcessor = new JwtTokenProcessor(jwtProperties);
                final String invalidToken = "Bearer abc.def.ghi";

                // when
                final Optional<Claims> actual = jwtProcessor.decode(TokenType.ACCESS, invalidToken);

                // then
                assertThat(actual).isEmpty();
            }
        }
    }
}
