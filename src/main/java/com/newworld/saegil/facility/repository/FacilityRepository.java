package com.newworld.saegil.facility.repository;

import com.newworld.saegil.facility.domain.Facility;
import com.newworld.saegil.facility.domain.FacilityInfoSource;
import com.newworld.saegil.location.GeoBoundingBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    @Query("select f.facilityCode from Facility f where f.infoSource = :infoSource")
    Set<String> findAllFacilityCodeByInfoSource(@Param("infoSource") final FacilityInfoSource infoSource);

    @Query("""
            SELECT new com.newworld.saegil.facility.repository.FacilityAndDistance(f,
                   (6371000 * acos(
                       cos(radians(:latitude)) * cos(radians(f.latitude)) *
                       cos(radians(f.longitude) - radians(:longitude)) +
                       sin(radians(:latitude)) * sin(radians(f.latitude))
                   ))) AS distance
            FROM Facility f
            WHERE f.latitude IS NOT NULL AND f.longitude IS NOT NULL
              AND (6371000 * acos(
                       cos(radians(:latitude)) * cos(radians(f.latitude)) *
                       cos(radians(f.longitude) - radians(:longitude)) +
                       sin(radians(:latitude)) * sin(radians(f.latitude))
                   )) <= :radius
            ORDER BY (6371000 * acos(
                                        cos(radians(:latitude)) * cos(radians(f.latitude)) *
                                        cos(radians(f.longitude) - radians(:longitude)) +
                                        sin(radians(:latitude)) * sin(radians(f.latitude))
                                    )) ASC
            """)
    List<FacilityAndDistance> findNearbyFacilities(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius
    );


    @Query("""
            SELECT f
            FROM Facility f
            WHERE f.latitude IS NOT NULL AND f.longitude IS NOT NULL
              AND f.latitude BETWEEN :#{#geoBoundingBox.minLatitude} AND :#{#geoBoundingBox.maxLatitude}
              AND f.longitude BETWEEN :#{#geoBoundingBox.minLongitude} AND :#{#geoBoundingBox.maxLongitude}
            """)
    List<Facility> findAllInBoundingBox(GeoBoundingBox geoBoundingBox);
}
