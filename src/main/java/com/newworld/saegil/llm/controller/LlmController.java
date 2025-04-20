package com.newworld.saegil.llm.controller;

import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.llm.service.LlmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/llm")
@RequiredArgsConstructor
@Tag(name = "LLM", description = "LLM 관련 API")
public class LlmController {

    private final LlmService llmService;

    @Operation(
            summary = "텍스트를 음성으로 변환",
            description = "ElevenLabs API를 사용하여 텍스트를 음성으로 변환합니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping(value = "/text-to-speech", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> textToSpeech(@RequestBody TextToSpeechRequest request) {
        log.info("Received text-to-speech request: {}", request.text());
        final Resource responseResource = llmService.textToSpeech(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"speech.mp3\"")
                .body(responseResource);
    }

    @Operation(
            summary = "오디오 URL에서 음성을 텍스트로 변환",
            description = "OpenAI Whisper API를 사용하여 오디오 URL에서 음성을 텍스트로 변환합니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping("/speech-to-text/audio-url")
    public ResponseEntity<SpeechToTextResponse> speechToTextFromUrl(@RequestBody SpeechToTextUrlRequest request) {
        log.info("Received speech-to-text URL request: {}", request.audioUrl());
        final String text = llmService.speechToTextFromAudioUrl(request);
        final SpeechToTextResponse response = new SpeechToTextResponse(text);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "파일에서 음성을 텍스트로 변환",
            description = "업로드된 MP3 파일에서 OpenAI Whisper API를 사용하여 음성을 텍스트로 변환합니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping(value = "/speech-to-text/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SpeechToTextResponse> speechToTextFromFile(@RequestPart("file") MultipartFile multipartFile) {
        log.info("Received speech-to-text file upload request: {}", multipartFile.getOriginalFilename());
        final String text = llmService.speechToTextFromAudioFile(multipartFile);
        final SpeechToTextResponse response = new SpeechToTextResponse(text);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "텍스트로부터 ChatGPT 응답 받기",
            description = "텍스트 입력을 기반으로 ChatGPT의 응답을 받습니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping("/chatgpt/text")
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
    @PostMapping("/chatgpt/stt-text")
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
    @PostMapping("/chatgpt/audio-url")
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
    @PostMapping(value = "/chatgpt/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ChatGptResponse> chatGptFromFile(@RequestPart("file") MultipartFile multipartFile) {
        log.info("Received ChatGPT file upload request: {}", multipartFile.getOriginalFilename());
        final String gptResponseText = llmService.receiveChatGptResponseFromAudioFile(multipartFile);
        final ChatGptResponse response = new ChatGptResponse(gptResponseText);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "MP3 파일로부터 ChatGPT 응답을 MP3 파일로 받기",
            description = "업로드된 MP3 파일을 기반으로 ChatGPT의 응답을 MP3 파일로 받습니다. " +
                    "이 API를 호출하려면 Authorization 헤더에 유효한 JWT 토큰이 필요합니다. " +
                    "토큰이 없거나 유효하지 않은 경우 401 Unauthorized 오류가 발생합니다.",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "MP3 파일 생성 성공",
                            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증 실패 - 로그인이 필요한 기능입니다. Authorization 헤더에 유효한 JWT 토큰을 추가해주세요."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류"
                    )
            }
    )
    @PostMapping(
            value = "/stt-chatgpt-tts",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<Resource> speechChatGptResponseFromFile(
            @RequestPart("file") MultipartFile multipartFile
    ) {
        log.info("Received STT-ChatGPT-TTS file upload request: {}", multipartFile.getOriginalFilename());
        Resource resource = llmService.receiveSttChatGptTtsResponseFromAudioFile(multipartFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"response.mp3\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
