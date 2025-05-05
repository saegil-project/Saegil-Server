package com.newworld.saegil.simulation.repository;

import com.newworld.saegil.simulation.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllBySimulationId(final Long simulationId);
}
