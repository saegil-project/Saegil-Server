package com.newworld.saegil.authentication.interceptor;

import com.newworld.saegil.authentication.domain.TokenType;
import com.newworld.saegil.authentication.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        if (isPreflightRequest(request)) {
            return true;
        }

        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (accessToken == null || accessToken.isEmpty()) {
            throw new LoginRequiredException("로그인이 필요한 기능입니다.");
        }

        authenticationService.getValidPrivateClaims(TokenType.ACCESS, accessToken);

        return true;
    }

    private boolean isPreflightRequest(final HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name()) &&
                request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD) != null;
    }
}
