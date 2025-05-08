package com.newworld.saegil.facility.repository;

import com.newworld.saegil.facility.domain.Facility;
import com.newworld.saegil.facility.domain.FacilityInfoSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    @Query("select f.facilityCode from Facility f where f.infoSource = :infoSource")
    Set<String> findAllFacilityCodeByInfoSource(@Param("infoSource") final FacilityInfoSource infoSource);

    @Query("SELECT f FROM Facility f WHERE f.latitude IS NOT NULL AND f.longitude IS NOT NULL")
    List<Facility> findAllByCoordinatesPresent();
}
