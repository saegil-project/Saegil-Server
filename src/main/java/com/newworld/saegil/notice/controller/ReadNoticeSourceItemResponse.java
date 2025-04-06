package com.newworld.saegil.notice.controller;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReadNoticeSourceItemResponse(

        @Schema(description = "기관 식별자", example = "1")
        Long id,

        @Schema(description = "기관 이름", example = "남북하나재단")
        String name
) {
}
