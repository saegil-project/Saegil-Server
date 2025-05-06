package com.newworld.saegil.organization.repository;

import com.newworld.saegil.organization.domain.Facility;
import com.newworld.saegil.organization.domain.FacilityInfoSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    @Query("select f.facilityCode from Facility f where f.infoSource = :infoSource")
    Set<String> findAllFacilityCodeByInfoSource(@Param("infoSource") final FacilityInfoSource infoSource);
}
