package com.newworld.saegil.organization.repository;

import com.newworld.saegil.organization.domain.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
}
