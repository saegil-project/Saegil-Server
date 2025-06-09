package com.newworld.saegil.facility.service;

import com.newworld.saegil.facility.domain.BusinessName;
import com.newworld.saegil.facility.domain.Facility;

public record NearbyFacilityDto(
        Long id,
        String name,
        BusinessName businessName,
        String telephoneNumber,
        String roadAddress,
        String jibunAddress,
        double latitude,
        double longitude,
        double distanceMeters
) {

    public static NearbyFacilityDto from(final Facility facility, final double distanceMeters) {
        return new NearbyFacilityDto(
                facility.getId(),
                facility.getName(),
                facility.getBusinessName(),
                facility.getTelephoneNumber(),
                facility.getRoadAddress(),
                facility.getJibunAddress(),
                facility.getLatitude(),
                facility.getLongitude(),
                distanceMeters
        );
    }
}
