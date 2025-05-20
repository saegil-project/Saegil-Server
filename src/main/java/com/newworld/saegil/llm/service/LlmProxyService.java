package com.newworld.saegil.llm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newworld.saegil.llm.config.ProxyProperties;
import com.newworld.saegil.llm.config.TtsProvider;
import com.newworld.saegil.simulation.service.SimulationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LlmProxyService implements AssistantService, TextToSpeechService {

    private final RestTemplate restTemplate;
    private final ProxyProperties proxyProperties;
    private final ObjectMapper objectMapper;
    private final SimulationService simulationService;

    @Override
    public AssistantResponse getAssistantTextResponseFromAudioFile(final MultipartFile multipartFile, final String threadId) {
        log.info("음성 파일로부터 어시스턴트 텍스트 응답을 가져옵니다: 파일명={}, 스레드 ID={}", multipartFile.getOriginalFilename(), threadId);

        try {
            final HttpHeaders requestHeader = new HttpHeaders();
            requestHeader.setContentType(MediaType.MULTIPART_FORM_DATA);

            final MultiValueMap<String, Object> requestBody = requestBodyFromMultiPartFile(multipartFile);
            final String assistantUploadPath = proxyProperties.assistantUploadPath();
            final String url = UriComponentsBuilder.fromUriString(assistantUploadPath)
                                                   .queryParamIfPresent(
                                                           "thread_id",
                                                           Optional.ofNullable(threadId).filter(s -> !s.isEmpty())
                                                   )
                                                   .build()
                                                   .toUriString();
            final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);
            final ResponseEntity<String> stringResponse = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (stringResponse.getStatusCode().is2xxSuccessful()) {
                final String responseBody = Objects.requireNonNull(
                        stringResponse.getBody(),
                        "Assistant API 응답 본문이 null 입니다."
                );
                log.info("LLM 서버로부터 받은 원시 응답: {}", responseBody);

                final AssistantResponse response = objectMapper.readValue(responseBody, AssistantResponse.class);

                String receivedThreadId = response.getThreadId();
                String userQuestion = response.getUserQuestion();
                String assistantAnswer = response.getAssistantAnswer();

                log.info("변환된 어시스턴트 응답 스레드 ID: {}", receivedThreadId);
                log.info("어시스턴트에 요청한 질문: {}", userQuestion);
                log.info("변환된 어시스턴트 응답: {}", assistantAnswer);

                return response;
            } else {
                log.error("어시스턴트 API 호출 실패: 상태 코드={}", stringResponse.getStatusCode());
                throw new RuntimeException("응답 생성 중 오류가 발생했습니다. 상태 코드: " + stringResponse.getStatusCode());
            }

        } catch (final Exception e) {
            log.error("어시스턴트 API 음성 파일 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("응답 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    public AssistantResponse getAssistantTextResponseFromAudioFile(
            final MultipartFile multipartFile,
            final String threadId,
            final Long scenarioId,
            final Long userId
    ) {
        log.info("음성 파일로부터 어시스턴트 텍스트 응답을 가져옵니다: 파일명={}, 스레드 ID={}, 시나리오 ID={}, 사용자 ID={}",
                multipartFile.getOriginalFilename(), threadId, scenarioId, userId);

        try {
            final HttpHeaders requestHeader = new HttpHeaders();
            requestHeader.setContentType(MediaType.MULTIPART_FORM_DATA);

            final MultiValueMap<String, Object> requestBody = requestBodyFromMultiPartFile(multipartFile);
            final String assistantUploadPath = proxyProperties.assistantUploadPath();
            final String url = UriComponentsBuilder.fromUriString(assistantUploadPath)
                                                   .queryParamIfPresent(
                                                           "thread_id",
                                                           Optional.ofNullable(threadId).filter(s -> !s.isEmpty())
                                                   )
                                                   .build()
                                                   .toUriString();
            final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);
            final ResponseEntity<String> stringResponse = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (stringResponse.getStatusCode().is2xxSuccessful()) {
                final String responseBody = Objects.requireNonNull(
                        stringResponse.getBody(),
                        "Assistant API 응답 본문이 null 입니다."
                );
                log.info("LLM 서버로부터 받은 원시 응답: {}", responseBody);

                final AssistantResponse response = objectMapper.readValue(responseBody, AssistantResponse.class);

                String receivedThreadId = response.getThreadId();
                String userQuestion = response.getUserQuestion();
                String assistantAnswer = response.getAssistantAnswer();

                log.info("변환된 어시스턴트 응답 스레드 ID: {}", receivedThreadId);
                log.info("어시스턴트에 요청한 질문: {}", userQuestion);
                log.info("변환된 어시스턴트 응답: {}", assistantAnswer);

                simulationService.createMessageCycle(receivedThreadId, scenarioId, userId, userQuestion, assistantAnswer);

                return response;
            } else {
                log.error("어시스턴트 API 호출 실패: 상태 코드={}", stringResponse.getStatusCode());
                throw new RuntimeException("응답 생성 중 오류가 발생했습니다. 상태 코드: " + stringResponse.getStatusCode());
            }

        } catch (final Exception e) {
            log.error("어시스턴트 API 음성 파일 호출 중 오류 발생: {}", e.getMessage(), e);
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

    @Override
    public Resource convertTextToSpeech(final String text, final TtsProvider provider) {
        log.info("LLM 서버에 TTS 요청을 전송합니다. 텍스트: {}, 프로바이더: {}", text, provider);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", text);

        final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(proxyProperties.textToSpeechPath())
                                                                 .queryParam("provider", provider.name().toLowerCase());
        final HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            final ResponseEntity<Resource> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    requestEntity,
                    Resource.class
            );
            log.info("LLM 서버로부터 TTS 응답을 수신했습니다.");
            return response.getBody();
        } catch (final Exception e) {
            log.error("TTS를 위해 LLM 서버 호출 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("TTS를 위해 LLM 서버 호출 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
