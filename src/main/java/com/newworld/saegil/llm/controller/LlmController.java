package com.newworld.saegil.llm.controller;

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

    // LLM 관련 최상위 컨트롤러
    // 세부 기능은 각 하위 컨트롤러(AssistantController, ChatGptController 등)로 분리되었습니다.
    // 이 컨트롤러는 향후 공통 LLM 기능이나 라우팅 로직이 필요할 경우 사용될 수 있습니다.
}
