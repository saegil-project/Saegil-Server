package com.newworld.saegil.facility.domain;

import lombok.Getter;

@Getter
public enum FacilityInfoSource {

    PUBLIC_DATA_NATIONAL_SOCIAL_WELFARE_FACILITY("공공데이터포털 전국사회복지시설표준데이터");

    private final String name;

    FacilityInfoSource(final String name) {
        this.name = name;
    }
}
