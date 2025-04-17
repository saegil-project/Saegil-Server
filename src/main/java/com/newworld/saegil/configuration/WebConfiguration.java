package com.newworld.saegil.configuration;

import com.newworld.saegil.authentication.interceptor.LoginInterceptor;
import com.newworld.saegil.authentication.resolver.AuthUserInfoArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final AuthUserInfoArgumentResolver authUserInfoArgumentResolver;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/v3/api-docs/**")
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/index.html")
                .excludePathPatterns("/test-llm-proxy.html")
                .excludePathPatterns("/api/v1/organizations/**")
                .excludePathPatterns("/api/v1/notices/**")
                .excludePathPatterns("/api/v1/scenarios/**")
                .excludePathPatterns("/api/v1/oauth2/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserInfoArgumentResolver);
    }
}
