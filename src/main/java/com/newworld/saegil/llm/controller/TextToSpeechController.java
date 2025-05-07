package com.newworld.saegil.llm.controller;

import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.llm.config.FileProperties;
import com.newworld.saegil.llm.config.TtsProvider;
import com.newworld.saegil.llm.model.TextToSpeechRequest;
import com.newworld.saegil.llm.service.TextToSpeechService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/llm/tts")
@RequiredArgsConstructor
@Tag(name = "TTS API", description = "Text-to-Speech API 관련 기능")
public class TextToSpeechController {

    private final TextToSpeechService textToSpeechService;
    private final FileProperties fileProperties;

    @Operation(
            summary = "텍스트를 음성으로 변환",
            description = "제공된 텍스트를 음성 파일로 변환합니다. `provider` 쿼리 파라미터로 음성 합성 엔진(openai, elevenlabs)을 지정할 수 있습니다.",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "음성 파일 생성 성공",
                            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증 실패 - 로그인이 필요한 기능입니다."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류"
                    )
            }
    )
    @PostMapping(value = "/", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> convertTextToSpeech(
            @Parameter(description = "음성으로 변환할 텍스트와 TTS 프로바이더 정보")
            @RequestBody final TextToSpeechRequest request,
            @Parameter(description = "음성 합성 엔진 (openai 또는 elevenlabs, 기본값: openai)")
            @RequestParam(value = "provider", required = false, defaultValue = "OPENAI") final TtsProvider provider) {
        log.info("Received TTS request. Text: {}, Provider: {}", request.getText(), provider);
        Resource audioResource = textToSpeechService.convertTextToSpeech(request.getText(), provider);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileProperties.ttsResultFileName() + "\"")
                .body(audioResource);
    }
}
