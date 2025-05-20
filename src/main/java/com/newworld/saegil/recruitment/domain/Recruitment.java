package com.newworld.saegil.recruitment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString
@Table(name = "recruitment")
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recruitmentCode;

    @Column(nullable = false)
    private RecruitmentInfoSource infoSource;

    @Column(nullable = false)
    private String name;

    private LocalDateTime recruitmentStartDate;

    private LocalDateTime recruitmentEndDate;

    private String weeklyWorkdays;

    private String workTime;

    private String pay;

    private String webLink;

    private String roadAddress;

    private String jibunAddress;

    private Double latitude;

    private Double longitude;

    private String errorMessage;

    public Recruitment(
            final String recruitmentCode,
            final RecruitmentInfoSource infoSource,
            final String name,
            final LocalDateTime recruitmentStartDate,
            final LocalDateTime recruitmentEndDate,
            final String weeklyWorkdays,
            final String workTime,
            final String pay,
            final String webLink,
            final String roadAddress
    ) {
        this.recruitmentCode = recruitmentCode;
        this.infoSource = infoSource;
        this.name = name;
        this.recruitmentStartDate = recruitmentStartDate;
        this.recruitmentEndDate = recruitmentEndDate;
        this.weeklyWorkdays = weeklyWorkdays;
        this.workTime = workTime;
        this.pay = pay;
        this.webLink = webLink;
        this.roadAddress = roadAddress;
    }
}
