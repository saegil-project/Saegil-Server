package com.newworld.saegil.configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    public static final String SERVICE_AUTH = "ServiceAuth";
    public static final String KAKAO_AUTH = "KakaoAuth";

    @Bean
    public OpenAPI openAPI() {
        final Info apiInfo = new Info().title("Saegil API Document").version("v0.0.1");
        return new OpenAPI()
                .info(apiInfo)
                .components(components())
                .addSecurityItem(new SecurityRequirement().addList(SERVICE_AUTH))
                .addSecurityItem(new SecurityRequirement().addList(KAKAO_AUTH));
    }

    private Components components() {
        final SecurityScheme serviceAuthScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("서비스에서 발급한 Bearer Token");

        final SecurityScheme kakaoAuthScheme = new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl("https://kauth.kakao.com/oauth/authorize") // 카카오 인증 URL
                                .tokenUrl("https://kauth.kakao.com/oauth/token") // 카카오 토큰 URL
                        ))
                .description("Kakao OAuth 인증");

        return new Components()
                .addSecuritySchemes(SERVICE_AUTH, serviceAuthScheme)
                .addSecuritySchemes(KAKAO_AUTH,kakaoAuthScheme);
    }
}
