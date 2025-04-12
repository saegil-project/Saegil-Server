package com.newworld.saegil.proxy.service;

import com.newworld.saegil.proxy.config.ProxyProperties;
import com.newworld.saegil.proxy.dto.request.ChatGptAudioUrlRequest;
import com.newworld.saegil.proxy.dto.request.ChatGptSttRequest;
import com.newworld.saegil.proxy.dto.request.ChatGptTextRequest;
import com.newworld.saegil.proxy.dto.request.SpeechToTextUrlRequest;
import com.newworld.saegil.proxy.dto.request.TextToSpeechRequest;
import com.newworld.saegil.proxy.dto.response.ChatGptResponse;
import com.newworld.saegil.proxy.dto.response.SpeechToTextResponse;
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
public class LlmProxyServiceImpl implements LlmProxyService {

    private final RestTemplate restTemplate;
    private final ProxyProperties proxyProperties;

    @Override
    public Resource textToSpeech(TextToSpeechRequest request) {
        log.info("Converting text to speech: {}", request.getText());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TextToSpeechRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<Resource> response = restTemplate.exchange(
                proxyProperties.getLlmServerUrl() + "/text-to-speech/",
                HttpMethod.POST,
                requestEntity,
                Resource.class
        );
        return response.getBody();
    }

    @Override
    public SpeechToTextResponse speechToTextFromUrl(SpeechToTextUrlRequest request) {
        log.info("Converting speech to text from URL: {}", request.getAudioUrl());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SpeechToTextUrlRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<SpeechToTextResponse> response = restTemplate.exchange(
                proxyProperties.getLlmServerUrl() + "/speech-to-text/",
                HttpMethod.POST,
                requestEntity,
                SpeechToTextResponse.class
        );

        return response.getBody();
    }

    @Override
    public SpeechToTextResponse speechToTextFromFile(MultipartFile multipartFile) {
        log.info("Converting speech to text from file: {}", multipartFile.getOriginalFilename());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        final MultipartInputStreamFileResource multipartInputStreamFileResource;
        try {
            multipartInputStreamFileResource = new MultipartInputStreamFileResource(multipartFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        body.add("file", multipartInputStreamFileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<SpeechToTextResponse> response = restTemplate.exchange(
                proxyProperties.getLlmServerUrl() + "/speech-to-text/upload",
                HttpMethod.POST,
                requestEntity,
                SpeechToTextResponse.class
        );

        return response.getBody();
    }

    @Override
    public ChatGptResponse chatGptFromText(ChatGptTextRequest request) {
        log.info("Getting ChatGPT response from text: {}", request.getText());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatGptTextRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ChatGptResponse> response = restTemplate.exchange(
                proxyProperties.getLlmServerUrl() + "/chatgpt/",
                HttpMethod.POST,
                entity,
                ChatGptResponse.class
        );

        return response.getBody();
    }

    @Override
    public ChatGptResponse chatGptFromStt(ChatGptSttRequest request) {
        log.info("Getting ChatGPT response from STT text: {}", request.getAudioText());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatGptSttRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ChatGptResponse> response = restTemplate.exchange(
                proxyProperties.getLlmServerUrl() + "/chatgpt/stt",
                HttpMethod.POST,
                entity,
                ChatGptResponse.class
        );

        return response.getBody();
    }

    @Override
    public ChatGptResponse chatGptFromAudioUrl(ChatGptAudioUrlRequest request) {
        log.info("Getting ChatGPT response from audio URL: {}", request.getAudioUrl());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatGptAudioUrlRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ChatGptResponse> response = restTemplate.exchange(
                proxyProperties.getLlmServerUrl() + "/chatgpt/audio",
                HttpMethod.POST,
                entity,
                ChatGptResponse.class
        );

        return response.getBody();
    }

    @Override
    public ChatGptResponse chatGptFromFile(MultipartFile multipartFile) {
        log.info("Getting ChatGPT response from audio file: {}", multipartFile.getOriginalFilename());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        final MultipartInputStreamFileResource multipartInputStreamFileResource;
        try {
            multipartInputStreamFileResource = new MultipartInputStreamFileResource(multipartFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        body.add("file", multipartInputStreamFileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<ChatGptResponse> response = restTemplate.exchange(
                proxyProperties.getLlmServerUrl() + "/chatgpt/upload",
                HttpMethod.POST,
                requestEntity,
                ChatGptResponse.class
        );

        return response.getBody();
    }
}
