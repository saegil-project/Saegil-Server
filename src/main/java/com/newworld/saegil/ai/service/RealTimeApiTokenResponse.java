package com.newworld.saegil.ai.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RealTimeApiTokenResponse {

    @JsonProperty("id")
    private String id;
    
    @JsonProperty("object")
    private String object;
    
    @JsonProperty("model")
    private String model;
    
    @JsonProperty("modalities")
    private List<String> modalities;
    
    @JsonProperty("instructions")
    private String instructions;
    
    @JsonProperty("voice")
    private String voice;
    
    @JsonProperty("input_audio_format")
    private String inputAudioFormat;
    
    @JsonProperty("output_audio_format")
    private String outputAudioFormat;
    
    @JsonProperty("input_audio_transcription")
    private InputAudioTranscription inputAudioTranscription;
    
    @JsonProperty("turn_detection")
    private Object turnDetection;
    
    @JsonProperty("tools")
    private List<Object> tools;
    
    @JsonProperty("tool_choice")
    private String toolChoice;
    
    @JsonProperty("temperature")
    private Double temperature;
    
    @JsonProperty("max_response_output_tokens")
    private String maxResponseOutputTokens;
    
    @JsonProperty("speed")
    private Double speed;
    
    @JsonProperty("tracing")
    private Object tracing;
    
    @JsonProperty("client_secret")
    private ClientSecret clientSecret;

    @JsonCreator
    public RealTimeApiTokenResponse(
        @JsonProperty("id") final String id,
        @JsonProperty("object") final String object,
        @JsonProperty("model") final String model,
        @JsonProperty("modalities") final List<String> modalities,
        @JsonProperty("instructions") final String instructions,
        @JsonProperty("voice") final String voice,
        @JsonProperty("input_audio_format") final String inputAudioFormat,
        @JsonProperty("output_audio_format") final String outputAudioFormat,
        @JsonProperty("input_audio_transcription") final InputAudioTranscription inputAudioTranscription,
        @JsonProperty("turn_detection") final Object turnDetection,
        @JsonProperty("tools") final List<Object> tools,
        @JsonProperty("tool_choice") final String toolChoice,
        @JsonProperty("temperature") final Double temperature,
        @JsonProperty("max_response_output_tokens") final String maxResponseOutputTokens,
        @JsonProperty("speed") final Double speed,
        @JsonProperty("tracing") final Object tracing,
        @JsonProperty("client_secret") final ClientSecret clientSecret
    ) {
        this.id = id;
        this.object = object;
        this.model = model;
        this.modalities = modalities;
        this.instructions = instructions;
        this.voice = voice;
        this.inputAudioFormat = inputAudioFormat;
        this.outputAudioFormat = outputAudioFormat;
        this.inputAudioTranscription = inputAudioTranscription;
        this.turnDetection = turnDetection;
        this.tools = tools;
        this.toolChoice = toolChoice;
        this.temperature = temperature;
        this.maxResponseOutputTokens = maxResponseOutputTokens;
        this.speed = speed;
        this.tracing = tracing;
        this.clientSecret = clientSecret;
    }

    @Getter
    @NoArgsConstructor
    public static class InputAudioTranscription {
        @JsonProperty("model")
        private String model;

        @JsonCreator
        public InputAudioTranscription(@JsonProperty("model") final String model) {
            this.model = model;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ClientSecret {
        @JsonProperty("value")
        private String value;
        
        @JsonProperty("expires_at")
        private Long expiresAt;

        @JsonCreator
        public ClientSecret(
            @JsonProperty("value") final String value,
            @JsonProperty("expires_at") final Long expiresAt
        ) {
            this.value = value;
            this.expiresAt = expiresAt;
        }
    }
}
