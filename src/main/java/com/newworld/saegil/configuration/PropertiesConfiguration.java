package com.newworld.saegil.configuration;

import com.newworld.saegil.security.jwt.JwtProperties;
import com.newworld.saegil.security.oauth2.KakaoOAuth2Properties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtProperties.class, KakaoOAuth2Properties.class})
public class PropertiesConfiguration {
}
