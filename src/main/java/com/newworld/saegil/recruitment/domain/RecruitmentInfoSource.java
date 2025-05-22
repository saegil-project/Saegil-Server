package com.newworld.saegil.recruitment.domain;

import lombok.Getter;

@Getter
public enum RecruitmentInfoSource {

    SEOUL_DATA_SEOUL_JOB_PORTAL("서울열린데이터광장 서울시 일자리포털 채용 정보");

    private final String name;

    RecruitmentInfoSource(final String name) {
        this.name = name;
    }
}
