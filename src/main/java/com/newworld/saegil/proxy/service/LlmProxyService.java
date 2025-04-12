package com.newworld.saegil.proxy.service;

import com.newworld.saegil.proxy.dto.request.ChatGptAudioUrlRequest;
import com.newworld.saegil.proxy.dto.request.ChatGptSttRequest;
import com.newworld.saegil.proxy.dto.request.ChatGptTextRequest;
import com.newworld.saegil.proxy.dto.request.SpeechToTextUrlRequest;
import com.newworld.saegil.proxy.dto.request.TextToSpeechRequest;
import com.newworld.saegil.proxy.dto.response.ChatGptResponse;
import com.newworld.saegil.proxy.dto.response.SpeechToTextResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for proxying requests to the FastAPI LLM server.
 */
public interface LlmProxyService {

    /**
     * Convert text to speech.
     *
     * @param request the text-to-speech request
     * @return a Mono containing the audio resource
     */
    Resource textToSpeech(TextToSpeechRequest request);

    /**
     * Convert speech to text using an audio URL.
     *
     * @param request the speech-to-text URL request
     * @return a Mono containing the speech-to-text response
     */
    SpeechToTextResponse speechToTextFromUrl(SpeechToTextUrlRequest request);

    /**
     * Convert speech to text using an uploaded file.
     *
     * @param multipartFile the uploaded file
     * @return a Mono containing the speech-to-text response
     */
    SpeechToTextResponse speechToTextFromFile(MultipartFile multipartFile);

    /**
     * Get ChatGPT response from text.
     *
     * @param request the ChatGPT text request
     * @return a Mono containing the ChatGPT response
     */
    ChatGptResponse chatGptFromText(ChatGptTextRequest request);

    /**
     * Get ChatGPT response from STT-converted text.
     *
     * @param request the ChatGPT STT request
     * @return a Mono containing the ChatGPT response
     */
    ChatGptResponse chatGptFromStt(ChatGptSttRequest request);

    /**
     * Get ChatGPT response from audio URL.
     *
     * @param request the ChatGPT audio URL request
     * @return a Mono containing the ChatGPT response
     */
    ChatGptResponse chatGptFromAudioUrl(ChatGptAudioUrlRequest request);

    /**
     * Get ChatGPT response from uploaded audio file.
     *
     * @param multipartFile the uploaded file
     * @return a Mono containing the ChatGPT response
     */
    ChatGptResponse chatGptFromFile(MultipartFile multipartFile);
}
