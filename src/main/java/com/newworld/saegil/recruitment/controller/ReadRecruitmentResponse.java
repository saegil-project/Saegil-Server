package com.newworld.saegil.recruitment.controller;

import com.newworld.saegil.recruitment.service.NearbyRecruitmentDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReadRecruitmentResponse(

        @Schema(description = "채용 정보 식별자", example = "1")
        Long id,

        @Schema(description = "채용 정보 제목", example = "연동지역아동센터 생활복지사 구인")
        String name,

        @Schema(description = "근무지명", example = "연동지역아동센터")
        String workPlaceName,

        @Schema(description = "근무 시간", example = "(오전) 9시 00분 ~ (오후) 6시 00분 (주 5일 근무)")
        String workTime,

        @Schema(description = "임금", example = "월 200만원")
        String pay,

        @Schema(description= "지원 기간", example = "2025-04-29 ~ 2025-05-14")
        String recruitmentPeriod,

        @Schema(description = "채용 정보 웹 링크", example = "http://ceu.ssis.go.kr/")
        String webLink,

        @Schema(description = "근무지 주소", example = "서울특별시 양천구 신월로11길 16 (신월동, 한빛종합사회복지관)")
        String address,

        @Schema(description = "위도", example = "37.5190344")
        double latitude,

        @Schema(description = "경도", example = "126.8402373")
        double longitude,

        @Schema(description = "사용자 위치로부터의 거리 (미터)", example = "312.5")
        double distanceMeters
) {

    public static ReadRecruitmentResponse from(final NearbyRecruitmentDto dto) {
        return new ReadRecruitmentResponse(
                dto.id(),
                dto.name(),
                dto.companyName(),
                dto.workTime() + " (" + dto.weeklyWorkdays() + ")",
                dto.pay(),
                String.format("%s ~ %s", dto.recruitmentStartDate(), dto.recruitmentEndDate()),
                dto.webLink(),
                dto.roadAddress(),
                dto.latitude(),
                dto.longitude(),
                dto.distanceMeters()
        );
    }
}
