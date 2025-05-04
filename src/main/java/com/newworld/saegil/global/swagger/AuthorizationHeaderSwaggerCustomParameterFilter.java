package com.newworld.saegil.global.swagger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthorizationHeaderSwaggerCustomParameterFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationParameterValue = request.getHeader(
                AuthorizationHeaderSwaggerCustomizer.AUTHORIZATION_HEADER_CUSTOM_PARAMETER_NAME
        );

        if (authorizationParameterValue != null) {
            HttpServletRequest requestWrapper = new HttpServletRequestWrapper(request) {
                @Override
                public String getHeader(String name) {
                    if (HttpHeaders.AUTHORIZATION.equalsIgnoreCase(name)) {
                        return authorizationParameterValue;
                    }
                    return super.getHeader(name);
                }
            };

            filterChain.doFilter(requestWrapper, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
