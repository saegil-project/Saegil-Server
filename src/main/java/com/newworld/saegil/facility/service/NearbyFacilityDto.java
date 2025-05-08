package com.newworld.saegil.facility.service;

import com.newworld.saegil.facility.domain.Facility;

public record NearbyFacilityDto(
        Long id,
        String name,
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
                facility.getTelephoneNumber(),
                facility.getRoadAddress(),
                facility.getJibunAddress(),
                facility.getLatitude(),
                facility.getLongitude(),
                distanceMeters
        );
    }
}
