package com.newworld.saegil.location.naver;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "geocoding.naver")
public record NaverGeocodingProperties(
        String clientId,
        String clientSecret,
        String apiUri
) {
}
