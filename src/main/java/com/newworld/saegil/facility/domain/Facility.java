package com.newworld.saegil.facility.domain;

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
import org.apache.commons.lang3.StringUtils;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString
@Table(name = "facility")
public class Facility {

    private static final double NO_LOCATION_COORDINATE = 9999.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String facilityCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityInfoSource infoSource;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusinessName businessName;

    @Column
    private String telephoneNumber;

    @Column
    private String roadAddress;

    @Column
    private String jibunAddress;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column
    private String errorMessage;

    public Facility(
            final String facilityCode,
            final FacilityInfoSource infoSource,
            final String name,
            final BusinessName businessName,
            final String telephoneNumber,
            final String roadAddress,
            final String jibunAddress
    ) {
        this.facilityCode = facilityCode.trim();
        this.infoSource = infoSource;
        this.name = name.trim();
        this.businessName = businessName;
        this.telephoneNumber = telephoneNumber.trim();
        this.roadAddress = roadAddress.trim();
        this.jibunAddress = jibunAddress.trim();
        this.latitude = NO_LOCATION_COORDINATE;
        this.longitude = NO_LOCATION_COORDINATE;
        this.errorMessage = null;
    }

    public void updateLocationInfo(final LocationInfo locationInfo) {
        this.roadAddress = locationInfo.roadAddress();
        this.jibunAddress = locationInfo.jibunAddress();
        this.latitude = locationInfo.getLatitude();
        this.longitude = locationInfo.getLongitude();
    }

    public String getJibunOrRoadAddress() {
        if (StringUtils.isBlank(roadAddress)) {
            return jibunAddress;
        }

        return roadAddress;
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

    public boolean hasFullName() {
        return StringUtils.isNotBlank(name) && !isNameMasked();
    }

    private boolean isNameMasked() {
        return name.startsWith("*") || name.endsWith("*") || name.contains("**");
    }
}
