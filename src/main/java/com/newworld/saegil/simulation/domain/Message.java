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
@ToString(of = {"id", "isFromUser", "contents", "createdAt"})
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "simulation_id", nullable = false, foreignKey = @ForeignKey(name = "fk_message_simulation"))
    private Simulation simulation;

    @Column(nullable = false)
    private boolean isFromUser;

    @Column(nullable = false, length = 16000)
    private String contents;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Message(final Simulation simulation, final boolean isFromUser, final String contents, final LocalDateTime createdAt) {
        if (contents.length() > 16000) {
            this.contents = contents.substring(0, 15990).concat("...");
        } else {
            this.contents = contents;
        }
        this.simulation = simulation;
        this.isFromUser = isFromUser;
        this.createdAt = createdAt;
    }
}
