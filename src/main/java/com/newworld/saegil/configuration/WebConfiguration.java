package com.newworld.saegil.configuration;

import com.newworld.saegil.authentication.interceptor.LoginInterceptor;
import com.newworld.saegil.authentication.resolver.AuthUserInfoArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final AuthUserInfoArgumentResolver authUserInfoArgumentResolver;
    private final CorsProperties corsProperties;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/error")
                .excludePathPatterns("/v3/api-docs/**")
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/index.html")
                .excludePathPatterns("/test-llm-proxy.html")
                .excludePathPatterns("/files/**")
                .excludePathPatterns("/api/v1/recruitments/**")
                .excludePathPatterns("/api/v1/facilities/**")
                .excludePathPatterns("/api/v1/organizations/**")
                .excludePathPatterns("/api/v1/notices/**")
                .excludePathPatterns("/api/v1/scenarios/**")
                .excludePathPatterns("/api/v1/news/categories")
                .excludePathPatterns("/api/v1/oauth2/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserInfoArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        final String[] allowedHttpMethods = Arrays.stream(HttpMethod.values())
                                                  .map(HttpMethod::name)
                                                  .toArray(String[]::new);

        registry.addMapping("/**")
                .allowedOrigins(corsProperties.allowedOrigins())
                .allowedHeaders("*")
                .allowedMethods(allowedHttpMethods).exposedHeaders(HttpHeaders.LOCATION)
                .allowCredentials(true);
    }
}
