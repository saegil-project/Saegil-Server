package com.newworld.saegil.proxy.service;

import java.time.Duration;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.newworld.saegil.proxy.dto.request.ChatGptAudioUrlRequest;
import com.newworld.saegil.proxy.dto.request.ChatGptSttRequest;
import com.newworld.saegil.proxy.dto.request.ChatGptTextRequest;
import com.newworld.saegil.proxy.dto.request.SpeechToTextUrlRequest;
import com.newworld.saegil.proxy.dto.request.TextToSpeechRequest;
import com.newworld.saegil.proxy.dto.response.ChatGptResponse;
import com.newworld.saegil.proxy.dto.response.SpeechToTextResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

/**
 * Implementation of the LlmProxyService interface.
 * This service proxies requests to the FastAPI LLM server.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LlmProxyServiceImpl implements LlmProxyService {

    private final WebClient llmServerWebClient;

    @Override
    public Mono<Resource> textToSpeech(TextToSpeechRequest request) {
        log.info("Converting text to speech: {}", request.getText());
        return llmServerWebClient.post()
                                 .uri("/text-to-speech/")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .body(BodyInserters.fromValue(request))
                                 .retrieve()
                                 .bodyToMono(Resource.class)
                                 .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                                 .doOnError(e -> log.error("Error converting text to speech", e));
    }

    @Override
    public Mono<SpeechToTextResponse> speechToTextFromUrl(SpeechToTextUrlRequest request) {
        log.info("Converting speech to text from URL: {}", request.getAudioUrl());
        return llmServerWebClient.post()
                                 .uri("/speech-to-text/")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .body(BodyInserters.fromValue(request))
                                 .retrieve()
                                 .bodyToMono(SpeechToTextResponse.class)
                                 .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                                 .doOnError(e -> log.error("Error converting speech to text from URL", e));
    }

    @Override
    public Mono<SpeechToTextResponse> speechToTextFromFile(FilePart filePart) {
        log.info("Converting speech to text from file: {}", filePart.filename());
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", filePart);

        return llmServerWebClient.post()
                                 .uri("/speech-to-text/upload")
                                 .contentType(MediaType.MULTIPART_FORM_DATA)
                                 .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                                 .retrieve()
                                 .bodyToMono(SpeechToTextResponse.class)
                                 .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                                 .doOnError(e -> log.error("Error converting speech to text from file", e));
    }

    @Override
    public Mono<ChatGptResponse> chatGptFromText(ChatGptTextRequest request) {
        log.info("Getting ChatGPT response from text: {}", request.getText());
        return llmServerWebClient.post()
                                 .uri("/chatgpt/")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .body(BodyInserters.fromValue(request))
                                 .retrieve()
                                 .bodyToMono(ChatGptResponse.class)
                                 .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                                 .doOnError(e -> log.error("Error getting ChatGPT response from text", e));
    }

    @Override
    public Mono<ChatGptResponse> chatGptFromStt(ChatGptSttRequest request) {
        log.info("Getting ChatGPT response from STT text: {}", request.getAudioText());
        return llmServerWebClient.post()
                                 .uri("/chatgpt/stt")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .body(BodyInserters.fromValue(request))
                                 .retrieve()
                                 .bodyToMono(ChatGptResponse.class)
                                 .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                                 .doOnError(e -> log.error("Error getting ChatGPT response from STT text", e));
    }

    @Override
    public Mono<ChatGptResponse> chatGptFromAudioUrl(ChatGptAudioUrlRequest request) {
        log.info("Getting ChatGPT response from audio URL: {}", request.getAudioUrl());
        return llmServerWebClient.post()
                                 .uri("/chatgpt/audio")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .body(BodyInserters.fromValue(request))
                                 .retrieve()
                                 .bodyToMono(ChatGptResponse.class)
                                 .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                                 .doOnError(e -> log.error("Error getting ChatGPT response from audio URL", e));
    }

    @Override
    public Mono<ChatGptResponse> chatGptFromFile(FilePart filePart) {
        log.info("Getting ChatGPT response from audio file: {}", filePart.filename());
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", filePart);

        return llmServerWebClient.post()
                                 .uri("/chatgpt/upload")
                                 .contentType(MediaType.MULTIPART_FORM_DATA)
                                 .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                                 .retrieve()
                                 .bodyToMono(ChatGptResponse.class)
                                 .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                                 .doOnError(e -> log.error("Error getting ChatGPT response from audio file", e));
    }
}
