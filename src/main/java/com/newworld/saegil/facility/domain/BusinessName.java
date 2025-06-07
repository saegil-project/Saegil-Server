package com.newworld.saegil.facility.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessName {

    CHILDREN_WELFARE("아동복지"),
    ELDERLY_WELFARE("노인복지"),
    DISABILITY_WELFARE("장애인복지"),
    WOMEN_FAMILY_WELFARE("여성&가족복지"),
    MEDICAL_WELFARE_EQUIPMENT("의료&복지용구"),
    OTHERS("기타");

    private final String koreanName;
}
