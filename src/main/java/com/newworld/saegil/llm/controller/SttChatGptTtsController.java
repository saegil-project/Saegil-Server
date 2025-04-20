package com.newworld.saegil.llm.controller;

import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.llm.service.SttChatGptTtsService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/llm/stt-chatgpt-tts")
@RequiredArgsConstructor
@Tag(name = "STT-ChatGPT-TTS API", description = "음성-텍스트-음성 통합 서비스 API")
public class SttChatGptTtsController {

    private final SttChatGptTtsService sttChatGptTtsService;

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
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<Resource> speechChatGptResponseFromFile(
            @RequestPart("file") MultipartFile multipartFile
    ) {
        log.info("Received STT-ChatGPT-TTS file upload request: {}", multipartFile.getOriginalFilename());
        Resource resource = sttChatGptTtsService.receiveSttChatGptTtsResponseFromAudioFile(multipartFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"response.mp3\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}