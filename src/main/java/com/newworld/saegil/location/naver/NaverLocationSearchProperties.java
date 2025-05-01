package com.newworld.saegil.location.naver;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "location-search.naver")
public record NaverLocationSearchProperties(
        String clientId,
        String clientSecret,
        String apiUri
) {
}
