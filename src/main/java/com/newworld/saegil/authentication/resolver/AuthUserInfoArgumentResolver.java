package com.newworld.saegil.authentication.resolver;

import com.newworld.saegil.authentication.annotation.AuthUser;
import com.newworld.saegil.authentication.domain.PrivateClaims;
import com.newworld.saegil.authentication.domain.TokenProcessor;
import com.newworld.saegil.authentication.domain.TokenType;
import com.newworld.saegil.authentication.dto.AuthUserInfo;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthUserInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenProcessor tokenProcessor;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUser.class) &&
                parameter.getParameterType() == AuthUserInfo.class;
    }

    @Override
    public Object resolveArgument(
            @NotNull final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            @NotNull final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final String accessToken = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("로그인이 필요한 기능입니다.");
        }

        final Claims claims = tokenProcessor.decode(TokenType.ACCESS, accessToken)
                                            .orElseThrow(() -> new IllegalArgumentException("유효한 토큰이 아닙니다."));
        final PrivateClaims privateClaims = PrivateClaims.from(claims);

        return new AuthUserInfo(privateClaims.userId());
    }
}
