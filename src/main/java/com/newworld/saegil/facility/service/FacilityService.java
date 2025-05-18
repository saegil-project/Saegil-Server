package com.newworld.saegil.facility.service;

import com.newworld.saegil.facility.repository.FacilityAndDistance;
import com.newworld.saegil.facility.repository.FacilityRepository;
import com.newworld.saegil.location.Coordinates;
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
            final Coordinates baseCoordinates,
            final int radius
    ) {
        final List<FacilityAndDistance> nearbyFacilities = facilityRepository.findNearbyFacilities(
                baseCoordinates.latitude(),
                baseCoordinates.longitude(),
                radius
        );

        return nearbyFacilities.stream()
                               .map(FacilityAndDistance::toNearbyFacilityDto)
                               .toList();
    }
}
