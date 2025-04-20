package com.newworld.saegil.llm.controller;

import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.llm.service.LlmService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/llm")
@RequiredArgsConstructor
@Tag(name = "LLM", description = "LLM 관련 API")
public class LlmController {

    private final LlmService llmService;
    
    // LLM 관련 최상위 컨트롤러
    // 세부 기능은 각 하위 컨트롤러로 분리되었습니다.
}
