package com.newworld.saegil.simulation.service;

import com.newworld.saegil.exception.UserNotFoundException;
import com.newworld.saegil.simulation.domain.Message;
import com.newworld.saegil.simulation.domain.Scenario;
import com.newworld.saegil.simulation.domain.Simulation;
import com.newworld.saegil.simulation.repository.MessageRepository;
import com.newworld.saegil.simulation.repository.ScenarioRepository;
import com.newworld.saegil.simulation.repository.SimulationRepository;
import com.newworld.saegil.user.domain.User;
import com.newworld.saegil.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SimulationService {

    private final UserRepository userRepository;
    private final ScenarioRepository scenarioRepository;
    private final SimulationRepository simulationRepository;
    private final MessageRepository messageRepository;

    public List<SimulationDto> readAllSimulationHistories(final Long userId) {
        final User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        return simulationRepository.findAllByUserId(user.getId())
                                   .stream()
                                   .map(SimulationDto::from)
                                   .toList();
    }

    public ReadMessagesResult readAllMessagesBySimulationId(final Long simulationId) {
        final Simulation simulation =
                simulationRepository.findById(simulationId)
                                    .orElseThrow(() ->
                                            new SimulationNotFoundException("해당하는 시뮬레이션 기록을 찾을 수 없습니다.")
                                    );
        final List<Message> messages = messageRepository.findAllBySimulationId(simulation.getId());

        return ReadMessagesResult.from(simulation, messages);
    }

    public CreateMessageResult createMessageCycle(
            final String threadId,
            final Long scenarioId,
            final Long userId,
            final String userQuestion,
            final String assistantAnswer
    ) {
        final User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        final Simulation simulation = simulationRepository.findByThreadIdAndUserId(threadId, userId)
                                                          .orElseGet(() -> createSimulation(threadId, scenarioId, user));
        final Message userQuestionMessage = new Message(simulation, true, userQuestion, LocalDateTime.now());
        messageRepository.save(userQuestionMessage);
        final Message assistantAnswerMessage = new Message(simulation, false, assistantAnswer, LocalDateTime.now());
        messageRepository.save(assistantAnswerMessage);

        return new CreateMessageResult(threadId, userQuestion, assistantAnswer);
    }

    private Simulation createSimulation(
            final String threadId,
            final Long scenarioId,
            final User user
    ) {
        final Scenario scenario = scenarioRepository.findById(scenarioId)
                                                    .orElseThrow(() ->
                                                            new ScenarioNotFoundException("시나리오가 존재하지 않습니다.")
                                                    );
        final Simulation newSimulation = new Simulation(scenario, user.getId(), threadId, LocalDateTime.now());

        return simulationRepository.save(newSimulation);
    }
}
