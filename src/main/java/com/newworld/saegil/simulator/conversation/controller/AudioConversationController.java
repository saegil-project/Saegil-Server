package com.newworld.saegil.simulator.conversation.controller;

import java.util.Map;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.simulator.conversation.service.AudioConversationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/stt")
@RequiredArgsConstructor
public class AudioConversationController {

    private final AudioConversationService audioConversationService;

    @Operation(
            summary = "오디오 대화",
            description = "오디오를 처리하고 오디오 응답을 반환합니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping(
            value = "/audio-to-audio",
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public Flux<DataBuffer> audioConversation(@RequestBody final byte[] audioFileBytes) {
        return audioConversationService.processAudioConversation(audioFileBytes);
    }

    @Operation(
            summary = "마지막 대화 가져오기",
            description = "마지막 대화 내용을 검색합니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping("/last-conversation")
    public ResponseEntity<Map<String, String>> getLastConversation() {
        return ResponseEntity.ok(audioConversationService.getLastConversation());
    }
}
