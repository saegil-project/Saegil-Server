package com.newworld.saegil.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RealTimeApiService implements RealTimeApi {

    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;
    
    private final RestTemplate restTemplate;

    private static final String OPENAI_REALTIME_API_URL = "https://api.openai.com/v1/realtime/sessions";
    
    @Override
    public RealTimeApiTokenResponse createEphemeralToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openaiApiKey);
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-realtime-preview-2025-06-03");
        requestBody.put("voice", "verse");
        requestBody.put("instructions", "You are a friendly assistant.");
        
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        
        try {
            ResponseEntity<RealTimeApiTokenResponse> responseEntity = 
                restTemplate.postForEntity(OPENAI_REALTIME_API_URL, requestEntity, RealTimeApiTokenResponse.class);
            
            return responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException("임시 토큰 생성에 실패했습니다: " + e.getMessage(), e);
        }
    }
}
