package com.newworld.saegil.llm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newworld.saegil.llm.config.ProxyProperties;
import com.newworld.saegil.llm.model.AssistantResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LlmProxyService implements AssistantService {

    private final RestTemplate restTemplate;
    private final ProxyProperties proxyProperties;
    private final ObjectMapper objectMapper;

    @Override
    public Resource getAssistantAudioResponseFromAudioFile(
            final MultipartFile multipartFile,
            final String threadId,
            final String provider
    ) {
        log.info("Getting Assistant audio response from audio file: {}, threadId: {}, provider: {}",
                multipartFile.getOriginalFilename(), threadId, provider);

        try {
            final HttpHeaders requestHeader = new HttpHeaders();
            requestHeader.setContentType(MediaType.MULTIPART_FORM_DATA);
            final MultiValueMap<String, Object> requestBody = requestBodyFromMultiPartFile(multipartFile);

            final String assistantAudioFromFilePath = proxyProperties.assistantAudioFromFilePath();

            final String url = UriComponentsBuilder.fromUriString(assistantAudioFromFilePath)
                    .queryParam("provider", provider != null ? provider : "openai")
                    .queryParamIfPresent("thread_id", Optional.ofNullable(threadId).filter(s -> !s.isEmpty()))
                    .build()
                    .toUriString();

            final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);

            final ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    byte[].class
            );

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                final byte[] responseBody = Objects.requireNonNull(responseEntity.getBody(), "Assistant Audio (Audio File) API 응답 본문이 null입니다.");
                if (responseBody.length == 0) {
                    log.warn("Assistant Audio (Audio File) API 응답 본문이 비어 있습니다.");
                }
                return new ByteArrayResource(responseBody);
            } else {
                log.error("Assistant Audio (Audio File) API call failed with status: {}", responseEntity.getStatusCode());
                throw new RuntimeException("오디오 응답 생성 중 오류가 발생했습니다. 상태 코드: " + responseEntity.getStatusCode());
            }

        } catch (final Exception e) {
            log.error("Assistant Audio API 오디오 파일 호출 중 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException("오디오 응답 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    public AssistantResponse getAssistantResponseFromAudioFile(final MultipartFile multipartFile, final String threadId) {
        log.info("Getting Assistant response from audio file: {}, threadId: {}",
                multipartFile.getOriginalFilename(), threadId);

        try {
            final HttpHeaders requestHeader = new HttpHeaders();
            requestHeader.setContentType(MediaType.MULTIPART_FORM_DATA);
            final MultiValueMap<String, Object> requestBody = requestBodyFromMultiPartFile(multipartFile);

            final String assistantUploadPath = proxyProperties.assistantUploadPath();

            final String url = UriComponentsBuilder.fromUriString(assistantUploadPath)
                    .queryParamIfPresent("thread_id", Optional.ofNullable(threadId).filter(s -> !s.isEmpty()))
                    .build()
                    .toUriString();

            final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);

            // 응답을 String으로 먼저 받아서 로깅
            final ResponseEntity<String> stringResponse = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (stringResponse.getStatusCode().is2xxSuccessful()) {
                final String responseBody = Objects.requireNonNull(stringResponse.getBody(), "Assistant API 응답 본문이 null입니다.");
                log.info("Raw response from LLM server: {}", responseBody);

                // String 응답을 AssistantResponse로 변환
                final AssistantResponse response = objectMapper.readValue(responseBody, AssistantResponse.class);
                log.info("Converted Assistant response: {}", response);
                log.info("Converted Assistant response thread_id: {}", response.getThreadId());
                return response;
            } else {
                log.error("Assistant API call failed with status: {}", stringResponse.getStatusCode());
                throw new RuntimeException("응답 생성 중 오류가 발생했습니다. 상태 코드: " + stringResponse.getStatusCode());
            }

        } catch (final Exception e) {
            log.error("Assistant API 오디오 파일 호출 중 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException("응답 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    private MultiValueMap<String, Object> requestBodyFromMultiPartFile(final MultipartFile multipartFile) {
        final MultipartInputStreamFileResource inputStreamFileResource;
        try {
            inputStreamFileResource = new MultipartInputStreamFileResource(multipartFile);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        final MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("file", inputStreamFileResource);

        return requestBody;
    }
}
