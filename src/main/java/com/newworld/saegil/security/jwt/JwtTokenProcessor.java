package com.newworld.saegil.security.jwt;

import com.newworld.saegil.authentication.domain.InvalidTokenException;
import com.newworld.saegil.authentication.domain.Token;
import com.newworld.saegil.authentication.domain.TokenProcessor;
import com.newworld.saegil.authentication.domain.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenProcessor implements TokenProcessor {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtProperties jwtProperties;

    @Override
    public String encode(
            final LocalDateTime issueTime,
            final TokenType tokenType,
            final Map<String, Object> privateClaims
    ) {
        final Date issueDate = convertToDate(issueTime);
        final String key = jwtProperties.findTokenKey(tokenType);
        final long expiredHours = jwtProperties.findExpiredHours(tokenType);

        return TOKEN_PREFIX + Jwts.builder()
                                  .issuedAt(issueDate)
                                  .expiration(new Date(issueDate.getTime() + expiredHours * 60 * 60 * 1000L))
                                  .claims(privateClaims)
                                  .signWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                                  .compact();
    }

    private Date convertToDate(final LocalDateTime targetTime) {
        return Date.from(targetTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public Optional<Claims> decode(final TokenType tokenType, final String token) {
        validateBearerToken(token);

        return parseToClaims(tokenType, token);
    }

    private void validateBearerToken(final String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException("토큰이 비어있습니다.");
        }

        if (!token.startsWith(TOKEN_PREFIX)) {
            throw new InvalidTokenException("Bearer 타입이 아닙니다.");
        }
    }

    private Optional<Claims> parseToClaims(final TokenType tokenType, final String token) {
        final String key = jwtProperties.findTokenKey(tokenType);
        try {
            final Claims claims = Jwts.parser()
                                      .verifyWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                                      .build()
                                      .parseSignedClaims(findPureToken(token))
                                      .getPayload();

            return Optional.of(claims);
        } catch (final JwtException ignored) {
            return Optional.empty();
        }
    }

    private String findPureToken(final String token) {
        return token.substring(TOKEN_PREFIX.length());
    }

    @Override
    public Token generateToken(final LocalDateTime issueTime, final Map<String, Object> privateClaims) {
        final String accessToken = encode(issueTime, TokenType.ACCESS, privateClaims);
        final String refreshToken = encode(issueTime, TokenType.REFRESH, privateClaims);

        return new Token(accessToken, refreshToken);
    }
}
