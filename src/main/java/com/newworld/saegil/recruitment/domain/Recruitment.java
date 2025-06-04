package com.newworld.saegil.recruitment.domain;

import com.newworld.saegil.location.GeoPoint;
import com.newworld.saegil.location.LocationInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecruitmentInfoSource infoSource;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String companyName;

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
            final String companyName,
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
        this.name = name.trim();
        this.companyName = companyName.trim();
        this.recruitmentStartDate = recruitmentStartDate;
        this.recruitmentEndDate = recruitmentEndDate;
        this.weeklyWorkdays = weeklyWorkdays.trim();
        this.workTime = workTime.trim();
        this.pay = pay.trim();
        this.webLink = webLink;
        this.roadAddress = roadAddress.trim();
    }

    public void updateLocationInfo(final LocationInfo locationInfo) {
        this.roadAddress = locationInfo.roadAddress();
        this.jibunAddress = locationInfo.jibunAddress();
        this.latitude = locationInfo.getLatitude();
        this.longitude = locationInfo.getLongitude();
    }


    public void markError(final Exception exception) {
        if (exception == null) {
            return;
        }

        final String exceptionMeesage = exception.getClass().getSimpleName() + ": " + exception.getMessage();

        if (exceptionMeesage.length() > 250) {
            this.errorMessage = exceptionMeesage.substring(0, 250) + "...";
        } else {
            this.errorMessage = exceptionMeesage;
        }
    }

    public GeoPoint getGeoPoint() {
        return new GeoPoint(latitude, longitude);
    }
}
