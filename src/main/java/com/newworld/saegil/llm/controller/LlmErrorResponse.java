package com.newworld.saegil.llm.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "오류 응답")
public class LlmErrorResponse {

    @Schema(description = "오류 메시지", example = "로그인이 필요한 기능입니다.")
    private String message;
}
