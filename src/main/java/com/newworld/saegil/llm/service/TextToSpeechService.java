package com.newworld.saegil.llm.service;

import com.newworld.saegil.llm.config.ProxyProperties;
import com.newworld.saegil.llm.config.TtsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextToSpeechService implements TextToSpeech {

    private final RestTemplate restTemplate;
    private final ProxyProperties proxyProperties;
    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;

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

    @Override
    public byte[] convertTextToSpeechV2(final String text) {
        return openAiAudioSpeechModel.call(text);
    }

}
