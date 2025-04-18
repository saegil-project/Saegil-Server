package com.newworld.saegil.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.newworld.saegil.llm.config.ProxyProperties;
import com.newworld.saegil.security.jwt.JwtProperties;
import com.newworld.saegil.security.oauth2.KakaoOAuth2Properties;

@Configuration
@EnableConfigurationProperties({JwtProperties.class, KakaoOAuth2Properties.class, ProxyProperties.class, CorsProperties.class})
public class PropertiesConfiguration {
}
