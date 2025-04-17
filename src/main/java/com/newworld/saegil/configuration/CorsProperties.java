package com.newworld.saegil.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.cors")
public record CorsProperties(
    String[] allowedOrigins
){
}
