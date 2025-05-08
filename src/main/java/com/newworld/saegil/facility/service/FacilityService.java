package com.newworld.saegil.facility.service;

import com.newworld.saegil.facility.domain.Facility;
import com.newworld.saegil.facility.repository.FacilityRepository;
import com.newworld.saegil.location.Coordinates;
import com.newworld.saegil.location.GeoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
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
        final List<Facility> allFacilities = facilityRepository.findAllByCoordinatesPresent();

        return allFacilities.stream()
                            .map(facility -> mapToDistanceAndFacility(baseCoordinates, facility))
                            .filter(distanceAndFacility -> distanceAndFacility.isWithinRadius(radius))
                            .sorted(Comparator.comparingDouble(DistanceAndFacility::distance))
                            .map(DistanceAndFacility::toNearbyFacilityDto)
                            .toList();
    }

    private DistanceAndFacility mapToDistanceAndFacility(Coordinates baseCoordinates, Facility facility) {
        final double distance = GeoUtils.calculateDistanceMeters(baseCoordinates, facility.getCoordinates());

        return new DistanceAndFacility(distance, facility);
    }

    public record DistanceAndFacility(
            double distance,
            Facility facility
    ) {

        public boolean isWithinRadius(int radius) {
            return distance <= radius;
        }

        public NearbyFacilityDto toNearbyFacilityDto() {
            return NearbyFacilityDto.from(facility, distance);
        }
    }
}
