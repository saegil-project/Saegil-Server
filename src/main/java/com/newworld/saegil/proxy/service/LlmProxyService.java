package com.newworld.saegil.proxy.service;

import com.newworld.saegil.proxy.config.ProxyProperties;
import com.newworld.saegil.proxy.controller.ChatGptAudioUrlRequest;
import com.newworld.saegil.proxy.controller.ChatGptSttRequest;
import com.newworld.saegil.proxy.controller.ChatGptTextRequest;
import com.newworld.saegil.proxy.controller.SpeechToTextUrlRequest;
import com.newworld.saegil.proxy.controller.TextToSpeechRequest;
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

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LlmProxyService implements LlmService {

    private final RestTemplate restTemplate;
    private final ProxyProperties proxyProperties;

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

        return responseEntity.getBody();
    }

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

        return responseEntity.getBody().text();
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

        return response.getBody().text();
    }

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

    @Override
    public String receiveChatGptResponseFromText(ChatGptTextRequest request) {
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

        return responseEntity.getBody().response();
    }

    @Override
    public String receiveChatGptResponseFromSttText(ChatGptSttRequest request) {
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

        return responseEntity.getBody().response();
    }

    @Override
    public String receiveChatGptResponseFromAudioUrl(ChatGptAudioUrlRequest request) {
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

        return responseEntity.getBody().response();
    }

    @Override
    public String receiveChatGptResponseFromAudioFile(MultipartFile multipartFile) {
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

        return responseEntity.getBody().response();
    }
}
