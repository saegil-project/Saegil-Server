package com.newworld.saegil.llm.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Text-to-Speech 요청 모델")
public class TextToSpeechRequest {
    @Schema(description = "음성으로 변환할 텍스트", example = "안녕하세요. 반갑습니다.")
    private String text;
}
