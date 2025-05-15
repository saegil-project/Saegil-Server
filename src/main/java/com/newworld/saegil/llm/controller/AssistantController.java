package com.newworld.saegil.llm.controller;

import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.llm.config.FileProperties;
import com.newworld.saegil.llm.config.TtsProvider;
import com.newworld.saegil.llm.model.AssistantResponse;
import com.newworld.saegil.llm.service.AssistantService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/llm/assistant")
@RequiredArgsConstructor
@Tag(name = "Assistant API", description = "OpenAI Assistant API 관련 기능")
public class AssistantController {

    private final AssistantService assistantService;
    private final FileProperties fileProperties;

    @Operation(
            summary = "음성 파일로부터 Assistant 응답을 음성으로 가져오기",
            description = """
                    업로드된 음성 파일에서 추출한 텍스트를 기반으로 OpenAI Assistant의 응답을 받고 이를 음성으로 변환합니다.\s
                    `thread_id` 쿼리 파라미터를 포함하면 기존 대화 상태를 유지할 수 있습니다 (선택 사항).\s
                    `provider` 쿼리 파라미터로 음성 합성 엔진(openai, elevenlabs)을 지정할 수 있습니다.\s
                    """,
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
    @PostMapping(
            value = "/upload/audio",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<Resource> getAssistantAudioResponseFromUploadedAudioFile(
            @RequestPart("file") final MultipartFile multipartFile,
            @RequestParam(value = "thread_id", required = false) final String threadId,
            @RequestParam(value = "provider", required = false, defaultValue = "OPENAI") final TtsProvider provider
    ) {
        log.info("Received Assistant audio file upload request: {}, threadId: {}, provider: {}",
                multipartFile.getOriginalFilename(), threadId, provider);
        final Resource responseResource = assistantService.getAssistantAudioResponseFromAudioFile(
                multipartFile,
                threadId,
                provider.name().toLowerCase()
        );
        return ResponseEntity.ok()
                             .header(
                                     HttpHeaders.CONTENT_DISPOSITION,
                                     "attachment; filename=\"" + fileProperties.resultFileName() + "\""
                             )
                             .contentType(MediaType.APPLICATION_OCTET_STREAM)
                             .body(responseResource);
    }

    @Operation(
            summary = "음성 파일로부터 Assistant 응답 가져오기",
            description = """
                    업로드된 음성 파일에서 추출한 텍스트를 기반으로 OpenAI Assistant의 응답을 받습니다.\s
                    `thread_id` 쿼리 파라미터를 포함하면 기존 대화 상태를 유지할 수 있습니다 (선택 사항).\s
                    """,
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Assistant 응답 성공",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
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
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AssistantResponse> getAssistantTextResponseFromUploadedAudioFile(
            @RequestPart("file") final MultipartFile multipartFile,
            @RequestParam(value = "thread_id", required = false) final String threadId
    ) {
        log.info(
                "Received Assistant audio file upload request: {}, threadId: {}",
                multipartFile.getOriginalFilename(), threadId
        );
        final AssistantResponse response = assistantService.getAssistantTextResponseFromAudioFile(multipartFile, threadId);
        log.info("Sending Assistant response with thread_id: {}", response.getThreadId());
        return ResponseEntity.ok(response);
    }
}
