package com.newworld.saegil.simulator.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO class for text-to-speech request
 */
@Setter
@Getter
public class TextToSpeechRequest {
	private String text;

	// Default constructor for JSON deserialization
	public TextToSpeechRequest() {
	}

	public TextToSpeechRequest(String text) {
		this.text = text;
	}

}
