package com.newworld.saegil.llm.service;

import com.newworld.saegil.llm.config.ProxyProperties;
import com.newworld.saegil.llm.controller.AssistantRequest;
import com.newworld.saegil.llm.controller.AssistantResponse;
import com.newworld.saegil.llm.controller.ChatGptAudioUrlRequest;
import com.newworld.saegil.llm.controller.ChatGptSttRequest;
import com.newworld.saegil.llm.controller.ChatGptTextRequest;
import com.newworld.saegil.llm.controller.SpeechToTextUrlRequest;
import com.newworld.saegil.llm.controller.TextToSpeechRequest;
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
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LlmProxyService implements TextToSpeechService, SpeechToTextService, ChatGptService, SttChatGptTtsService, AssistantService {

    private final RestTemplate restTemplate;
    private final ProxyProperties proxyProperties;

    // --- TextToSpeechService Implementation ---
    @Override
    public Resource textToSpeech(TextToSpeechRequest request) {
        log.info("Converting text to speech: {}", request.text());

        final HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_JSON);
        final ProxyTextToSpeechRequest requestBody = new ProxyTextToSpeechRequest(request.text());
        final HttpEntity<ProxyTextToSpeechRequest> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        final ResponseEntity<Resource> responseEntity = restTemplate.exchange(
                proxyProperties.getTtsPath(),
                HttpMethod.POST,
                requestEntity,
                Resource.class
        );

        return Objects.requireNonNull(responseEntity.getBody(), "TTS API 응답 본문이 null입니다.");
    }

    // --- SpeechToTextService Implementation ---
    @Override
    public String speechToTextFromAudioUrl(SpeechToTextUrlRequest request) {
        log.info("Converting speech to text from URL: {}", request.audioUrl());

        final HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_JSON);
        final ProxySpeechToTextUrlRequest requestBody = new ProxySpeechToTextUrlRequest(request.audioUrl());
        final HttpEntity<ProxySpeechToTextUrlRequest> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        final ResponseEntity<ProxySpeechToTextResponse> responseEntity = restTemplate.exchange(
                proxyProperties.getSttPath(),
                HttpMethod.POST,
                requestEntity,
                ProxySpeechToTextResponse.class
        );

        ProxySpeechToTextResponse body = Objects.requireNonNull(responseEntity.getBody(), "STT (URL) API 응답 본문이 null입니다.");
        log.debug("STT (URL) API response text: {}", body.text());
        return body.text();
    }

    @Override
    public String speechToTextFromAudioFile(MultipartFile multipartFile) {
        log.info("Converting speech to text from file: {}", multipartFile.getOriginalFilename());

        final HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.MULTIPART_FORM_DATA);
        final MultiValueMap<String, Object> requestBody = requestBodyFromMultiPartFile(multipartFile);
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        final ResponseEntity<ProxySpeechToTextResponse> response = restTemplate.exchange(
                proxyProperties.getSttFromAudioFilePath(),
                HttpMethod.POST,
                requestEntity,
                ProxySpeechToTextResponse.class
        );

        ProxySpeechToTextResponse body = Objects.requireNonNull(response.getBody(), "STT (File) API 응답 본문이 null입니다.");
        log.debug("STT (File) API response text: {}", body.text());
        return body.text();
    }

    // --- ChatGptService Implementation ---
    @Override
    public String receiveChatGptResponseFromText(final ChatGptTextRequest request) {
        log.info("Getting ChatGPT response from text: {}", request.text());

        final HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_JSON);
        final ProxyChatGptTextRequest requestBody = new ProxyChatGptTextRequest(request.text());
        final HttpEntity<ProxyChatGptTextRequest> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        final ResponseEntity<ProxyChatGptResponse> responseEntity = restTemplate.exchange(
                proxyProperties.getChatgptFromTextPath(),
                HttpMethod.POST,
                requestEntity,
                ProxyChatGptResponse.class
        );

        ProxyChatGptResponse body = Objects.requireNonNull(responseEntity.getBody(), "ChatGPT (Text) API 응답 본문이 null입니다.");
        log.debug("ChatGPT (Text) API response: {}", body.response());
        return body.response();
    }

    @Override
    public String receiveChatGptResponseFromSttText(final ChatGptSttRequest request) {
        log.info("Getting ChatGPT response from STT text: {}", request.audioText());

        final HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_JSON);
        final ProxyChatGptSttRequest requestBody = new ProxyChatGptSttRequest(request.audioText());
        final HttpEntity<ProxyChatGptSttRequest> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        final ResponseEntity<ProxyChatGptResponse> responseEntity = restTemplate.exchange(
                proxyProperties.getChatgptFromSttTextPath(),
                HttpMethod.POST,
                requestEntity,
                ProxyChatGptResponse.class
        );

        ProxyChatGptResponse body = Objects.requireNonNull(responseEntity.getBody(), "ChatGPT (STT Text) API 응답 본문이 null입니다.");
        log.debug("ChatGPT (STT Text) API response: {}", body.response());
        return body.response();
    }

    @Override
    public String receiveChatGptResponseFromAudioUrl(final ChatGptAudioUrlRequest request) {
        log.info("Getting ChatGPT response from audio URL: {}", request.audioUrl());

        final HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_JSON);
        final ProxyChatGptAudioUrlRequest requestBody = new ProxyChatGptAudioUrlRequest(request.audioUrl());
        final HttpEntity<ProxyChatGptAudioUrlRequest> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        final ResponseEntity<ProxyChatGptResponse> responseEntity = restTemplate.exchange(
                proxyProperties.getChatgptFromAudioUrlPath(),
                HttpMethod.POST,
                requestEntity,
                ProxyChatGptResponse.class
        );

        ProxyChatGptResponse body = Objects.requireNonNull(responseEntity.getBody(), "ChatGPT (Audio URL) API 응답 본문이 null입니다.");
        log.debug("ChatGPT (Audio URL) API response: {}", body.response());
        return body.response();
    }

    @Override
    public String receiveChatGptResponseFromAudioFile(final MultipartFile multipartFile) {
        log.info("Getting ChatGPT response from audio file: {}", multipartFile.getOriginalFilename());

        final HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.MULTIPART_FORM_DATA);
        final MultiValueMap<String, Object> requestBody = requestBodyFromMultiPartFile(multipartFile);
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        final ResponseEntity<ProxyChatGptResponse> responseEntity = restTemplate.exchange(
                proxyProperties.getChatgptFromAudioFilePath(),
                HttpMethod.POST,
                requestEntity,
                ProxyChatGptResponse.class
        );

        ProxyChatGptResponse body = Objects.requireNonNull(responseEntity.getBody(), "ChatGPT (Audio File) API 응답 본문이 null입니다.");
        log.debug("ChatGPT (Audio File) API response: {}", body.response());
        return body.response();
    }

    // --- SttChatGptTtsService Implementation ---
    @Override
    public Resource receiveSttChatGptTtsResponseFromAudioFile(final MultipartFile multipartFile) {
        log.info("Getting STT ChatGPT TTS response from audio file: {}", multipartFile.getOriginalFilename());

        final HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.MULTIPART_FORM_DATA);
        final MultiValueMap<String, Object> requestBody = requestBodyFromMultiPartFile(multipartFile);
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);

        final ResponseEntity<Resource> responseEntity = restTemplate.exchange(
                proxyProperties.getSttChatgptTtsFilePath(),
                HttpMethod.POST,
                requestEntity,
                Resource.class
        );

        return Objects.requireNonNull(responseEntity.getBody(), "STT-ChatGPT-TTS API 응답 본문이 null입니다.");
    }

    // --- AssistantService Implementation ---
    @Override
    public AssistantResponse getAssistantResponse(AssistantRequest request, String threadId) {
        log.info("Getting Assistant response for text: {}, threadId: {}", request.text(), threadId);

        try {
            final HttpHeaders requestHeader = new HttpHeaders();
            requestHeader.setContentType(MediaType.APPLICATION_JSON);
            final ProxyAssistantRequest requestBody = new ProxyAssistantRequest(request.text());
            final HttpEntity<ProxyAssistantRequest> requestEntity = new HttpEntity<>(requestBody, requestHeader);

            String assistantPath = proxyProperties.getAssistantPath();
            if (!assistantPath.endsWith("/")) {
                assistantPath += "/";
            }

            final String url = UriComponentsBuilder.fromUriString(assistantPath)
                    .queryParamIfPresent("thread_id", Optional.ofNullable(threadId).filter(s -> !s.isEmpty()))
                    .build().toUriString();

            final ResponseEntity<ProxyAssistantResponse> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    ProxyAssistantResponse.class
            );

            final ProxyAssistantResponse proxyResponse = Objects.requireNonNull(responseEntity.getBody(), "Assistant API 응답 본문이 null입니다.");
            log.debug("Assistant API response: response='{}', threadId='{}', text='{}'", proxyResponse.response(), proxyResponse.threadId(), proxyResponse.text());

            return new AssistantResponse(
                    proxyResponse.response(),
                    proxyResponse.threadId(),
                    proxyResponse.text()
            );
        } catch (Exception e) {
            log.error("Assistant API 호출 중 에러 발생: {}", e.getMessage(), e);
            return new AssistantResponse("서비스 연결 중 오류가 발생했습니다. 다시 시도해주세요.",
                    threadId != null ? threadId : "", request.text());
        }
    }

    @Override
    public AssistantResponse getAssistantResponseFromAudioFile(final MultipartFile multipartFile, String threadId) {
        log.info("Getting Assistant response from audio file: {}, threadId: {}", multipartFile.getOriginalFilename(), threadId);

        try {
            final HttpHeaders requestHeader = new HttpHeaders();
            requestHeader.setContentType(MediaType.MULTIPART_FORM_DATA);
            final MultiValueMap<String, Object> requestBody = requestBodyFromMultiPartFile(multipartFile);

            String assistantAudioFilePath = proxyProperties.getAssistantFromAudioFilePath();

            final String url = UriComponentsBuilder.fromUriString(assistantAudioFilePath)
                    .queryParamIfPresent("thread_id", Optional.ofNullable(threadId).filter(s -> !s.isEmpty()))
                    .build().toUriString();

            final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);

            final ResponseEntity<ProxyAssistantResponse> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    ProxyAssistantResponse.class
            );

            final ProxyAssistantResponse proxyResponse = Objects.requireNonNull(responseEntity.getBody(), "Assistant (Audio File) API 응답 본문이 null입니다.");
            log.debug("Assistant (Audio File) API response: response='{}', threadId='{}', text='{}'", proxyResponse.response(), proxyResponse.threadId(), proxyResponse.text());

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return new AssistantResponse(
                        proxyResponse.response(),
                        proxyResponse.threadId(),
                        proxyResponse.text()
                );
            } else {
                log.error("Assistant (Audio File) API call failed with status: {} and body: {}", responseEntity.getStatusCode(), proxyResponse);
                return new AssistantResponse("서비스 연결 중 오류가 발생했습니다. 상태 코드: " + responseEntity.getStatusCode(),
                        threadId != null ? threadId : "", "[오디오 파일 입력]");
            }

        } catch (Exception e) {
            log.error("Assistant API 오디오 파일 호출 중 에러 발생: {}", e.getMessage(), e);
            return new AssistantResponse("서비스 연결 중 오류가 발생했습니다. 다시 시도해주세요.",
                    threadId != null ? threadId : "", "[오디오 파일 입력]");
        }
    }

    @Override
    public Resource getAssistantAudioResponse(AssistantRequest request, String threadId, String provider) {
        log.info("Getting Assistant audio response for text: {}, threadId: {}, provider: {}", request.text(), threadId, provider);

        try {
            final HttpHeaders requestHeader = new HttpHeaders();
            requestHeader.setContentType(MediaType.APPLICATION_JSON);
            final ProxyAssistantRequest requestBody = new ProxyAssistantRequest(request.text());
            final HttpEntity<ProxyAssistantRequest> requestEntity = new HttpEntity<>(requestBody, requestHeader);

            String assistantAudioPath = proxyProperties.getAssistantAudioPath();
            if (!assistantAudioPath.endsWith("/")) {
                assistantAudioPath += "/";
            }

            final String url = UriComponentsBuilder.fromUriString(assistantAudioPath)
                    .queryParam("provider", provider != null ? provider : "openai")
                    .queryParamIfPresent("thread_id", Optional.ofNullable(threadId).filter(s -> !s.isEmpty()))
                    .build().toUriString();

            final ResponseEntity<Resource> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Resource.class
            );

            Resource responseResource = Objects.requireNonNull(responseEntity.getBody(), "Assistant Audio API 응답 본문이 null입니다.");

            return responseResource;
        } catch (Exception e) {
            log.error("Assistant Audio API 호출 중 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException("오디오 응답 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    public Resource getAssistantAudioResponseFromAudioFile(final MultipartFile multipartFile, String threadId, String provider) {
        log.info("Getting Assistant audio response from audio file: {}, threadId: {}, provider: {}", multipartFile.getOriginalFilename(), threadId, provider);

        try {
            final HttpHeaders requestHeader = new HttpHeaders();
            requestHeader.setContentType(MediaType.MULTIPART_FORM_DATA);
            final MultiValueMap<String, Object> requestBody = requestBodyFromMultiPartFile(multipartFile);

            String assistantAudioFromFilePath = proxyProperties.getAssistantAudioFromFilePath();

            final String url = UriComponentsBuilder.fromUriString(assistantAudioFromFilePath)
                    .queryParam("provider", provider != null ? provider : "openai")
                    .queryParamIfPresent("thread_id", Optional.ofNullable(threadId).filter(s -> !s.isEmpty()))
                    .build().toUriString();

            final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, requestHeader);

            final ResponseEntity<Resource> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Resource.class
            );

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                return responseEntity.getBody();
            } else {
                log.error("Assistant Audio (Audio File) API call failed with status: {} and body: {}", responseEntity.getStatusCode(), responseEntity.getBody());
                throw new RuntimeException("오디오 응답 생성 중 오류가 발생했습니다. 상태 코드: " + responseEntity.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Assistant Audio API 오디오 파일 호출 중 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException("오디오 응답 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    // --- Helper Methods ---
    private MultiValueMap<String, Object> requestBodyFromMultiPartFile(final MultipartFile multipartFile) {
        MultipartInputStreamFileResource inputStreamFileResource;
        try {
            inputStreamFileResource = new MultipartInputStreamFileResource(multipartFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("file", inputStreamFileResource);

        return requestBody;
    }
}
