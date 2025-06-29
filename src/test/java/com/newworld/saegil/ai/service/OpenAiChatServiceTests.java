package com.newworld.saegil.ai.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class OpenAiChatServiceTests {

    @Autowired
    private OpenAiChatService openAiChatService;

    @Test
    @DisplayName("샘플 오디오 파일을 사용한 OpenAI API 응답 테스트")
    public void testGetOpenAiResponseWithSampleAudio() throws Exception {
        final MockMultipartFile mockMultipartFile = createSampleAudioFile();
        
        final byte[] response = openAiChatService.getOpenAiResponse(mockMultipartFile);
        
        assertNotNull(response);
        assertTrue(response.length > 0);
    }

    private MockMultipartFile createSampleAudioFile() throws IOException {
        final ClassPathResource resource = new ClassPathResource("com/newworld/saegil/ai/service/SampleQuestion.mp3");
        final InputStream inputStream = resource.getInputStream();
        final byte[] audioData = inputStream.readAllBytes();
        inputStream.close();
        
        return new MockMultipartFile(
            "audioFile",
            "SampleQuestion.mp3",
            "audio/mp3",
            audioData
        );
    }
}
