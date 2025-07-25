package com.newworld.saegil.simulation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = {"id", "createdAt"})
@Table(name = "simulation")
public class Simulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scenario_id", nullable = false, foreignKey = @ForeignKey(name = "fk_simulation_scenario"))
    private Scenario scenario;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String threadId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Simulation(final Scenario scenario, Long userId, final String threadId, final LocalDateTime createdAt) {
        this.scenario = scenario;
        this.userId = userId;
        this.threadId = threadId;
        this.createdAt = createdAt;
    }
}
