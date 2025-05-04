package com.newworld.saegil.simulation.service;

import com.newworld.saegil.exception.UserNotFoundException;
import com.newworld.saegil.simulation.repository.SimulationRepository;
import com.newworld.saegil.user.domain.User;
import com.newworld.saegil.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SimulationService {

    private final UserRepository userRepository;
    private final SimulationRepository simulationRepository;

    public List<SimulationDto> readAllSimulationHistories(final Long userId) {
        final User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        return simulationRepository.findAllByUserId(user.getId())
                                   .stream()
                                   .map(SimulationDto::from)
                                   .toList();
    }
}
