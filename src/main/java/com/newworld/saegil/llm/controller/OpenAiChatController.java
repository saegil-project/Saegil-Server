package com.newworld.saegil.llm.controller;

import com.newworld.saegil.llm.service.OpenAiChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "OpenAI Chat API", description = "OpenAI Chat with Spring AI")
public class OpenAiChatController {

    private final OpenAiChatService openAiChatService;

    @PostMapping(
            value = "/v1/chat/audio",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<byte[]> getOpenAiResponse(@RequestPart("file") final MultipartFile multipartFile) {
        try {
            byte[] response = openAiChatService.getOpenAiResponse(multipartFile);
            
            String filename = "response_audio_" + System.currentTimeMillis() + ".mp3";
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(response.length))
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                    .header(HttpHeaders.PRAGMA, "no-cache")
                    .header(HttpHeaders.EXPIRES, "0")
                    .body(response);
        } catch (IllegalArgumentException e) {
            log.error("잘못된 요청: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}