package com.newworld.saegil.security.filter;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.newworld.saegil.authentication.domain.InvalidTokenException;
import com.newworld.saegil.authentication.domain.TokenProcessor;
import com.newworld.saegil.authentication.domain.TokenType;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * WebFilter to validate authentication tokens for LLM API endpoints.
 * This filter ensures that only authenticated users (Kakao-logged-in users) can access the LLM API.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationWebFilter implements WebFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String LLM_API_PATH = "/api/v1/llm";

    private final TokenProcessor tokenProcessor;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // Only apply filter to LLM API endpoints
        if (!path.startsWith(LLM_API_PATH)) {
            return chain.filter(exchange);
        }

        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
            log.error("Authorization header is missing or invalid for LLM API request");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            Optional<Claims> claims = tokenProcessor.decode(TokenType.ACCESS, authorizationHeader);

            if (claims.isEmpty()) {
                log.error("Invalid token: could not decode claims");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // Store user ID in exchange attributes for controllers to use if needed
            Long userId = claims.get().get("userId", Long.class);
            exchange.getAttributes().put("userId", userId);

            log.info("Authenticated user with ID {} accessing LLM API", userId);
            return chain.filter(exchange);
        } catch (InvalidTokenException e) {
            log.error("Invalid token: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        } catch (Exception e) {
            log.error("Error validating token", e);
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }
    }
}
