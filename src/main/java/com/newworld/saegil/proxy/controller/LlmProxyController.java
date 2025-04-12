package com.newworld.saegil.proxy.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.proxy.dto.request.ChatGptAudioUrlRequest;
import com.newworld.saegil.proxy.dto.request.ChatGptSttRequest;
import com.newworld.saegil.proxy.dto.request.ChatGptTextRequest;
import com.newworld.saegil.proxy.dto.request.SpeechToTextUrlRequest;
import com.newworld.saegil.proxy.dto.request.TextToSpeechRequest;
import com.newworld.saegil.proxy.dto.response.ChatGptResponse;
import com.newworld.saegil.proxy.dto.response.SpeechToTextResponse;
import com.newworld.saegil.proxy.service.LlmProxyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * FastAPI LLM 서버로 요청을 프록시하는 컨트롤러.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/llm")
@RequiredArgsConstructor
@Tag(name = "LLM 프록시", description = "FastAPI LLM 서버로 요청을 프록시하는 엔드포인트")
public class LlmProxyController {

    private final LlmProxyService llmProxyService;

    @Operation(
            summary = "텍스트를 음성으로 변환",
            description = "ElevenLabs API를 사용하여 텍스트를 음성으로 변환합니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping(value = "/text-to-speech", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Mono<ResponseEntity<Resource>> textToSpeech(@RequestBody TextToSpeechRequest request) {
        log.info("Received text-to-speech request: {}", request.getText());
        return llmProxyService.textToSpeech(request)
                              .map(resource -> ResponseEntity.ok()
                                                             .header(HttpHeaders.CONTENT_DISPOSITION,
                                                                     "attachment; filename=\"speech.mp3\"")
                                                             .body(resource));
    }

    @Operation(
            summary = "URL에서 음성을 텍스트로 변환",
            description = "OpenAI Whisper API를 사용하여 오디오 URL에서 음성을 텍스트로 변환합니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping("/speech-to-text/url")
    public Mono<SpeechToTextResponse> speechToTextFromUrl(@RequestBody SpeechToTextUrlRequest request) {
        log.info("Received speech-to-text URL request: {}", request.getAudioUrl());
        return llmProxyService.speechToTextFromUrl(request);
    }

    @Operation(
            summary = "파일에서 음성을 텍스트로 변환",
            description = "업로드된 오디오 파일에서 OpenAI Whisper API를 사용하여 음성을 텍스트로 변환합니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping(value = "/speech-to-text/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<SpeechToTextResponse> speechToTextFromFile(@RequestPart("file") FilePart filePart) {
        log.info("Received speech-to-text file upload request: {}", filePart.filename());
        return llmProxyService.speechToTextFromFile(filePart);
    }

    @Operation(
            summary = "텍스트로부터 ChatGPT 응답 받기",
            description = "텍스트 입력을 기반으로 ChatGPT의 응답을 받습니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping("/chatgpt/text")
    public Mono<ChatGptResponse> chatGptFromText(@RequestBody ChatGptTextRequest request) {
        log.info("Received ChatGPT text request: {}", request.getText());
        return llmProxyService.chatGptFromText(request);
    }

    @Operation(
            summary = "STT 텍스트로부터 ChatGPT 응답 받기",
            description = "STT로 변환된 텍스트를 기반으로 ChatGPT의 응답을 받습니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping("/chatgpt/stt")
    public Mono<ChatGptResponse> chatGptFromStt(@RequestBody ChatGptSttRequest request) {
        log.info("Received ChatGPT STT request: {}", request.getAudioText());
        return llmProxyService.chatGptFromStt(request);
    }

    @Operation(
            summary = "오디오 URL로부터 ChatGPT 응답 받기",
            description = "URL의 오디오를 기반으로 ChatGPT의 응답을 받습니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping("/chatgpt/audio-url")
    public Mono<ChatGptResponse> chatGptFromAudioUrl(@RequestBody ChatGptAudioUrlRequest request) {
        log.info("Received ChatGPT audio URL request: {}", request.getAudioUrl());
        return llmProxyService.chatGptFromAudioUrl(request);
    }

    @Operation(
            summary = "오디오 파일로부터 ChatGPT 응답 받기",
            description = "업로드된 오디오 파일을 기반으로 ChatGPT의 응답을 받습니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping(value = "/chatgpt/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ChatGptResponse> chatGptFromFile(@RequestPart("file") FilePart filePart) {
        log.info("Received ChatGPT file upload request: {}", filePart.filename());
        return llmProxyService.chatGptFromFile(filePart);
    }
}
