package com.newworld.saegil.simulator.stt.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.simulator.stt.service.SpeechToTextService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/stt")
@RequiredArgsConstructor
public class AudioTranscriptionController {

    private final SpeechToTextService speechToTextService;

    @Operation(
            summary = "Transcribe audio",
            description = "Transcribe audio file to text",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping(value = "/transcribe", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Mono<ResponseEntity<Map<String, String>>> transcribeAudio(
            @RequestBody byte[] audioFileBytes
    ) {
        return speechToTextService.transcribeAndGetAnswer(audioFileBytes)
                                  .map(ResponseEntity::ok);
    }
}
