package com.newworld.saegil.organization.domain;

import com.newworld.saegil.location.LocationInfo;
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

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString
@Table(name = "facility")
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String facilityCode;

    @Column(nullable = false)
    private FacilityInfoSource infoSource;

    @Column(nullable = false)
    private String name;

    @Column
    private String telephoneNumber;

    @Column
    private String roadAddress;

    @Column
    private String jibunAddress;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    public Facility(
            final String facilityCode,
            final FacilityInfoSource infoSource,
            final String name,
            final String telephoneNumber,
            final String roadAddress,
            final String jibunAddress,
            final Double latitude,
            final Double longitude
    ) {
        this.name = name.trim();
        this.facilityCode = facilityCode.trim();
        this.infoSource = infoSource;
        this.telephoneNumber = telephoneNumber.trim();
        this.roadAddress = roadAddress.trim();
        this.jibunAddress = jibunAddress.trim();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updateLocationInfo(final LocationInfo locationInfo) {
        this.roadAddress = locationInfo.roadAddress();
        this.jibunAddress = locationInfo.jibunAddress();
        this.latitude = locationInfo.getLatitude();
        this.longitude = locationInfo.getLongitude();
    }
}
