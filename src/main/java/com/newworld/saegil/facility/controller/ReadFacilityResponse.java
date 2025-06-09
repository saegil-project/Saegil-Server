package com.newworld.saegil.facility.controller;

import com.newworld.saegil.facility.domain.BusinessName;
import com.newworld.saegil.facility.service.NearbyFacilityDto;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;

public record ReadFacilityResponse(

        @Schema(description = "시설 식별자", example = "1")
        Long id,

        @Schema(description = "시설명", example = "한빛종합사회복지관")
        String name,

        @Schema(description = "업종명", example = "CHILDREN_WELFARE")
        BusinessName businessName,

        @Schema(description = "위도", example = "37.5190344")
        double latitude,

        @Schema(description = "경도", example = "126.8402373")
        double longitude,

        @Schema(description = "시설 전화번호", example = "02-2690-8762~4")
        String telephoneNumber,

        @Schema(description = "시설 주소", example = "서울특별시 양천구 신월로11길 16 (신월동, 한빛종합사회복지관)")
        String address,

        @Schema(description = "사용자 위치로부터의 거리 (미터)", example = "312.5")
        double distance
) {

    public static ReadFacilityResponse from(final NearbyFacilityDto dto) {
        String address = dto.roadAddress();
        if (StringUtils.isBlank(dto.roadAddress())) {
            address = dto.jibunAddress();
        }

        return new ReadFacilityResponse(
                dto.id(),
                dto.name(),
                dto.businessName(),
                dto.latitude(),
                dto.longitude(),
                dto.telephoneNumber(),
                address,
                dto.distanceMeters()
        );
    }
}
