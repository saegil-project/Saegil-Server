package com.newworld.saegil.authentication.domain;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import io.jsonwebtoken.Claims;

public interface TokenProcessor {

    String encode(final LocalDateTime issueTime, final TokenType tokenType, final Map<String, Object> privateClaims);

    Optional<Claims> decode(final TokenType tokenType, final String token);

    Token generateToken(final LocalDateTime issueTime, final Map<String, Object> privateClaims);
}
