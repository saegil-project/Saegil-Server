package com.newworld.saegil.llm.controller;

import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.llm.service.AssistantService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// Enum 정의 추가
enum TtsProvider {
    OPENAI, ELEVENLABS
}

@Slf4j
@RestController
@RequestMapping("/api/v1/llm/assistant")
@RequiredArgsConstructor
@Tag(name = "Assistant API", description = "OpenAI Assistant API 관련 기능")
public class AssistantController {

    private final AssistantService assistantService;

    @Operation(
            summary = "텍스트 쿼리에 대한 Assistant 응답 가져오기",
            description = "텍스트 쿼리를 기반으로 OpenAI Assistant의 응답을 받습니다. `thread_id` 쿼리 파라미터를 포함하면 기존 대화 상태를 유지할 수 있습니다 (선택 사항).",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping
    public ResponseEntity<AssistantResponse> getAssistantResponse(
            @Parameter(description = "텍스트 쿼리") @RequestBody AssistantRequest request,
            @Parameter(description = "기존 대화 스레드 ID (선택 사항)") @RequestParam(value = "thread_id", required = false) String threadId
    ) {
        log.info("Received Assistant request: text='{}', threadId='{}'", request.text(), threadId);
        AssistantResponse response = assistantService.getAssistantResponse(request, threadId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "MP3 파일로부터 Assistant 응답 받기",
            description = "업로드된 MP3 파일에서 추출한 텍스트를 기반으로 OpenAI Assistant의 응답을 받습니다. `thread_id` 쿼리 파라미터를 포함하면 기존 대화 상태를 유지할 수 있습니다 (선택 사항).",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AssistantResponse> getAssistantResponseFromFile(
            @Parameter(description = "음성 파일 (MP3 등)") @RequestPart("file") MultipartFile multipartFile,
            @Parameter(description = "기존 대화 스레드 ID (선택 사항)") @RequestParam(value = "thread_id", required = false) String threadId
    ) {
        log.info("Received Assistant file upload request: {}, threadId: {}",
                multipartFile.getOriginalFilename(), threadId);
        AssistantResponse response = assistantService.getAssistantResponseFromAudioFile(multipartFile, threadId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "텍스트 쿼리에 대한 Assistant 응답을 음성으로 가져오기",
            description = "텍스트 쿼리를 기반으로 OpenAI Assistant의 응답을 받고 이를 음성으로 변환합니다. `thread_id` 쿼리 파라미터를 포함하면 기존 대화 상태를 유지할 수 있습니다 (선택 사항). `provider` 쿼리 파라미터로 음성 합성 엔진(openai, elevenlabs)을 지정할 수 있습니다.",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping(value = "/audio", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getAssistantAudioResponse(
            @Parameter(description = "텍스트 쿼리") @RequestBody AssistantRequest request,
            @Parameter(description = "기존 대화 스레드 ID (선택 사항)") @RequestParam(value = "thread_id", required = false) String threadId,
            @Parameter(description = "음성 합성 엔진 (openai 또는 elevenlabs, 기본값: openai)") @RequestParam(value = "provider", required = false, defaultValue = "OPENAI") TtsProvider provider
    ) {
        log.info("Received Assistant audio request: text='{}', threadId='{}', provider: {}", request.text(), threadId, provider);
        Resource responseResource = assistantService.getAssistantAudioResponse(request, threadId, provider.name().toLowerCase());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"assistant_response.mp3\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(responseResource);
    }

    @Operation(
            summary = "MP3 파일로부터 Assistant 응답을 음성으로 가져오기",
            description = "업로드된 MP3 파일에서 추출한 텍스트를 기반으로 OpenAI Assistant의 응답을 받고 이를 음성으로 변환합니다. `thread_id` 쿼리 파라미터를 포함하면 기존 대화 상태를 유지할 수 있습니다 (선택 사항). `provider` 쿼리 파라미터로 음성 합성 엔진(openai, elevenlabs)을 지정할 수 있습니다.",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "MP3 파일 생성 성공",
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
    public ResponseEntity<Resource> getAssistantAudioResponseFromFile(
            @Parameter(description = "음성 파일 (MP3 등)") @RequestPart("file") MultipartFile multipartFile,
            @Parameter(description = "기존 대화 스레드 ID (선택 사항)") @RequestParam(value = "thread_id", required = false) String threadId,
            @Parameter(description = "음성 합성 엔진 (openai 또는 elevenlabs, 기본값: openai)") @RequestParam(value = "provider", required = false, defaultValue = "OPENAI") TtsProvider provider
    ) {
        log.info("Received Assistant audio file upload request: {}, threadId: {}, provider: {}",
                multipartFile.getOriginalFilename(), threadId, provider);
        Resource responseResource = assistantService.getAssistantAudioResponseFromAudioFile(multipartFile, threadId, provider.name().toLowerCase());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"assistant_response.mp3\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(responseResource);
    }
}