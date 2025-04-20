package com.newworld.saegil.llm.controller;

import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.llm.service.LlmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/llm/chatgpt")
@RequiredArgsConstructor
@Tag(name = "ChatGPT API", description = "ChatGPT 관련 API")
public class ChatGptController {

    private final LlmService llmService;

    @Operation(
            summary = "텍스트로부터 ChatGPT 응답 받기",
            description = "텍스트 입력을 기반으로 ChatGPT의 응답을 받습니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping("/text")
    public ResponseEntity<ChatGptResponse> chatGptFromText(@RequestBody ChatGptTextRequest request) {
        log.info("Received ChatGPT text request: {}", request.text());
        final String gptResponseText = llmService.receiveChatGptResponseFromText(request);
        final ChatGptResponse response = new ChatGptResponse(gptResponseText);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "STT 텍스트로부터 ChatGPT 응답 받기",
            description = "STT로 변환된 텍스트를 기반으로 ChatGPT의 응답을 받습니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping("/stt-text")
    public ResponseEntity<ChatGptResponse> chatGptFromStt(@RequestBody ChatGptSttRequest request) {
        log.info("Received ChatGPT STT request: {}", request.audioText());
        final String gptResponseText = llmService.receiveChatGptResponseFromSttText(request);
        final ChatGptResponse response = new ChatGptResponse(gptResponseText);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "오디오 URL로부터 ChatGPT 응답 받기",
            description = "URL의 오디오를 기반으로 ChatGPT의 응답을 받습니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping("/audio-url")
    public ResponseEntity<ChatGptResponse> chatGptFromAudioUrl(@RequestBody ChatGptAudioUrlRequest request) {
        log.info("Received ChatGPT audio URL request: {}", request.audioUrl());
        final String gptResponseText = llmService.receiveChatGptResponseFromAudioUrl(request);
        final ChatGptResponse response = new ChatGptResponse(gptResponseText);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "MP3 파일로부터 ChatGPT 응답 받기",
            description = "업로드된 MP3 파일을 기반으로 ChatGPT의 응답을 받습니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ChatGptResponse> chatGptFromFile(@RequestPart("file") MultipartFile multipartFile) {
        log.info("Received ChatGPT file upload request: {}", multipartFile.getOriginalFilename());
        final String gptResponseText = llmService.receiveChatGptResponseFromAudioFile(multipartFile);
        final ChatGptResponse response = new ChatGptResponse(gptResponseText);

        return ResponseEntity.ok(response);
    }
}