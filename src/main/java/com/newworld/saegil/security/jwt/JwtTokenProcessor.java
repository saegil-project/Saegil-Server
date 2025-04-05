package com.newworld.saegil.security.jwt;

import com.newworld.saegil.authentication.domain.Token;
import com.newworld.saegil.authentication.domain.TokenProcessor;
import com.newworld.saegil.authentication.domain.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
        final ZonedDateTime zonedDateTime = targetTime.atZone(ZoneId.of("Asia/Seoul"));

        return Date.from(zonedDateTime.toInstant());
    }

    @Override
    public Optional<Claims> decode(final TokenType tokenType, final String token) {
        return Optional.empty();
    }

    @Override
    public Token generateToken(final LocalDateTime issueTime, final Map<String, Object> privateClaims) {
        return null;
    }
}
