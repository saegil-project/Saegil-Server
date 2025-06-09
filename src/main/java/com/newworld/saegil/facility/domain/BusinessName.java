package com.newworld.saegil.facility.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessName {

    CHILDREN_WELFARE("아동"),
    ELDERLY_WELFARE("노인"),
    DISABILITY_WELFARE("장애인"),
    WOMEN_FAMILY_WELFARE("여성&가족"),
    MEDICAL_WELFARE_EQUIPMENT("의료&복지용구"),
    OTHERS("기타");

    private final String koreanName;
}
