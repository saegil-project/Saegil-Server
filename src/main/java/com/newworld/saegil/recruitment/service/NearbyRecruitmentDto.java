package com.newworld.saegil.recruitment.service;

import com.newworld.saegil.recruitment.domain.Recruitment;

import java.time.LocalDateTime;

public record NearbyRecruitmentDto(
        Long id,
        String name,
        String companyName,
        LocalDateTime recruitmentStartDate,
        LocalDateTime recruitmentEndDate,
        String weeklyWorkdays,
        String workTime,
        String pay,
        String webLink,
        String roadAddress,
        double latitude,
        double longitude,
        double distanceMeters
) {

    public static NearbyRecruitmentDto from(final Recruitment recruitment, final double distanceMeters) {
        return new NearbyRecruitmentDto(
                recruitment.getId(),
                recruitment.getName(),
                recruitment.getCompanyName(),
                recruitment.getRecruitmentStartDate(),
                recruitment.getRecruitmentEndDate(),
                recruitment.getWeeklyWorkdays(),
                recruitment.getWorkTime(),
                recruitment.getPay(),
                recruitment.getWebLink(),
                recruitment.getRoadAddress(),
                recruitment.getLatitude(),
                recruitment.getLongitude(),
                distanceMeters
        );
    }
}
