package com.newworld.saegil.llm.controller;

import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.llm.config.FileProperties;
import com.newworld.saegil.llm.service.TextToSpeechService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/llm/tts")
@RequiredArgsConstructor
@Tag(name = "TTS API", description = "Text-to-Speech 관련 기능")
public class TextToSpeechController {

    private final TextToSpeechService textToSpeechService;
    private final FileProperties fileProperties;

    @Operation(
            summary = "텍스트를 음성으로 변환",
            description = "제공된 텍스트를 음성 파일로 변환합니다. `provider` 필드를 통해 음성 합성 엔진(openai, elevenlabs)을 선택할 수 있습니다.",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "음성 파일 생성 성공",
                            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
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
    @PostMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> convertTextToSpeech(@RequestBody final TextToSpeechRequest request) {
        log.info("TTS 요청 수신: {}, 제공자: {}", request.text(), request.provider());
        final Resource audioResource = textToSpeechService.convertTextToSpeech(request.text(), request.provider());
        return ResponseEntity.ok()
                             .header(
                                     HttpHeaders.CONTENT_DISPOSITION,
                                     "attachment; filename=\"" + fileProperties.ttsResultFileName() + "\""
                             )
                             .contentType(MediaType.APPLICATION_OCTET_STREAM)
                             .body(audioResource);
    }
}
