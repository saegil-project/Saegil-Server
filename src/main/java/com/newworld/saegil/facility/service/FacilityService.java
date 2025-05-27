package com.newworld.saegil.facility.service;

import com.newworld.saegil.facility.repository.FacilityAndDistance;
import com.newworld.saegil.facility.repository.FacilityRepository;
import com.newworld.saegil.location.GeoPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public List<NearbyFacilityDto> readNearbyFacilities(
            final GeoPoint baseGeoPoint,
            final int radius
    ) {
        final List<FacilityAndDistance> nearbyFacilities = facilityRepository.findNearbyFacilities(
                baseGeoPoint.latitude(),
                baseGeoPoint.longitude(),
                radius
        );

        return nearbyFacilities.stream()
                               .map(FacilityAndDistance::toNearbyFacilityDto)
                               .toList();
    }
}
