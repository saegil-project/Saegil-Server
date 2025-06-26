package com.newworld.saegil.llm.controller;

import com.newworld.saegil.authentication.annotation.AuthUser;
import com.newworld.saegil.authentication.dto.AuthUserInfo;
import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.llm.service.AssistantResponse;
import com.newworld.saegil.llm.service.Assistant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Assistant API", description = "OpenAI Assistant API 관련 기능")
public class AssistantController {

    private final Assistant assistant;

    @Operation(
            summary = "음성 파일로부터 Assistant 응답 가져오기 (V1)",
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
            value = "/v1/llm/assistant/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AssistantResponse> getAssistantTextResponseFromUploadedAudioFile(
            @RequestPart("file") final MultipartFile multipartFile,
            @Parameter(description = "스레드 ID", example = "th_123123")
            @RequestParam(value = "thread_id", required = false) final String threadId
    ) {
        log.info(
                "Received Assistant audio file upload request: {}, threadId: {}",
                multipartFile.getOriginalFilename(), threadId
        );
        final AssistantResponse response = assistant.getAssistantTextResponseFromAudioFile(multipartFile, threadId);
        log.info("Sending Assistant response with thread_id: {}", response.threadId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "음성 파일로부터 Assistant 응답 가져오기 (V2)",
            description = """
                    업로드된 음성 파일에서 추출한 텍스트를 기반으로 OpenAI Assistant의 응답을 받습니다.\s
                    `thread_id` 쿼리 파라미터를 포함하면 기존 대화 상태를 유지할 수 있습니다 (선택 사항).\s
                    `scenario_id` 쿼리 파라미터로 시뮬레이션 시나리오를 지정할 수 있습니다 (필수).\s
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
            value = "/v2/llm/assistant/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AssistantResponse> getAssistantTextResponseFromUploadedAudioFile(
            @RequestPart("file") final MultipartFile multipartFile,
            @Parameter(description = "스레드 ID", example = "th_123123")
            @RequestParam(value = "thread_id", required = false) final String threadId,
            @Parameter(description = "시뮬레이션 시나리오 ID", example = "1")
            @RequestParam(value = "scenario_id") final Long scenarioId,
            @AuthUser final AuthUserInfo authUserInfo
    ) {
        log.info(
                "Received Assistant audio file upload request (V2): {}, threadId: {}, scenarioId: {}, userId: {}",
                multipartFile.getOriginalFilename(), threadId, scenarioId, authUserInfo.userId()
        );
        final AssistantResponse response = assistant.getAssistantTextResponseFromAudioFile(
                multipartFile,
                threadId,
                scenarioId,
                authUserInfo.userId()
        );
        log.info("Sending Assistant response with thread_id: {}", response.threadId());
        return ResponseEntity.ok(response);
    }
}
