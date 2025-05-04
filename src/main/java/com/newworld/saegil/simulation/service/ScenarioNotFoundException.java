package com.newworld.saegil.simulation.service;

import com.newworld.saegil.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ScenarioNotFoundException extends CustomException {

    public ScenarioNotFoundException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
