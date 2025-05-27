package com.newworld.saegil.facility.service;

import com.newworld.saegil.facility.domain.Facility;
import com.newworld.saegil.facility.repository.FacilityAndDistance;
import com.newworld.saegil.facility.repository.FacilityRepository;
import com.newworld.saegil.location.GeoBoundingBox;
import com.newworld.saegil.location.GeoPoint;
import com.newworld.saegil.location.GeoUtils;
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
        final GeoBoundingBox geoBoundingBox = GeoUtils.calculateBoundingBox(
                baseGeoPoint.latitude(),
                baseGeoPoint.longitude(),
                radius
        );
        final List<Facility> facilitiesInBoundingBox = facilityRepository.findAllInBoundingBox(geoBoundingBox);

        return facilitiesInBoundingBox.stream()
                               .map(facility -> mapWithDistance(facility, baseGeoPoint))
                               .filter(facilityAndDistance -> facilityAndDistance.distance() <= radius)
                               .map(FacilityAndDistance::toNearbyFacilityDto)
                               .toList();
    }

    private FacilityAndDistance mapWithDistance(final Facility facility, final GeoPoint baseGeoPoint) {
        return new FacilityAndDistance(
                facility,
                GeoUtils.calculateDistanceMeters(baseGeoPoint, facility.getGeoPoint())
        );
    }
}
