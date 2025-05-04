package com.newworld.saegil.simulation.repository;

import com.newworld.saegil.simulation.domain.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimulationRepository extends JpaRepository<Simulation, Long> {

    List<Simulation> findAllByUserId(final Long userId);
}
