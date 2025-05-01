package com.newworld.saegil.organization.controller;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReadOrganizationResponse(

        @Schema(description = "기관 식별자", example = "1")
        Long id,

        @Schema(description = "기관명", example = "한빛종합사회복지관")
        String name,

        @Schema(description = "위도", example = "37.5190344")
        double latitude,

        @Schema(description = "경도", example = "126.8402373")
        double longitude,

        @Schema(description = "운영시간", example = "09:00 ~ 17:00")
        String operatingHours,

        @Schema(description = "기관 전화번호", example = "02-2690-8762~4")
        String telephoneNumber,

        @Schema(description = "기관 주소", example = "서울특별시 양천구 신월로11길 16 (신월동, 한빛종합사회복지관)")
        String address,

        @Schema(description = "사용자 위치로부터의 거리 (미터)", example = "312.5")
        double distance
) {
}
