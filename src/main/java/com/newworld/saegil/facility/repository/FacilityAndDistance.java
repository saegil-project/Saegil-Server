package com.newworld.saegil.facility.repository;

import com.newworld.saegil.facility.domain.Facility;
import com.newworld.saegil.facility.service.NearbyFacilityDto;

public record FacilityAndDistance(
        Facility facility,
        double distance
) {

    public NearbyFacilityDto toNearbyFacilityDto() {
        return NearbyFacilityDto.from(facility, distance);
    }
}
