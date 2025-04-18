package com.newworld.saegil.simulation.service;

import com.newworld.saegil.simulation.repository.ScenarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;

    public List<ScenarioDto> readAll() {
        return scenarioRepository.findAll()
                                 .stream()
                                 .map(ScenarioDto::from)
                                 .toList();
    }
}
