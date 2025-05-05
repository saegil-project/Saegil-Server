package com.newworld.saegil.simulation.service;

import com.newworld.saegil.exception.CustomException;
import org.springframework.http.HttpStatus;

public class SimulationNotFoundException extends CustomException {

    public SimulationNotFoundException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
