package com.epam.audioProcessor.controller;

import com.epam.audioProcessor.service.AudioTextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AudioTextControllerTest {

    @Mock
    private AudioTextService audioTextService;

    @InjectMocks
    private AudioTextController audioTextController;

    @BeforeEach
    void setUp() {
        audioTextService = mock(AudioTextService.class);
        audioTextController = new AudioTextController();
        audioTextController.setAudioTextService(audioTextService);
    }

    @Test
    void testHandleAudioUpload_withValidWavFile() throws Exception {
        // Arrange
        MockMultipartFile wavFile = new MockMultipartFile(
                "file",
                "voice.wav",
                "audio/wav",
                "dummy content".getBytes()
        );

        when(audioTextService.getTextFromAudio(any(MultipartFile.class)))
                .thenReturn(ResponseEntity.ok("Test transcription"));

        ResponseEntity<?> response = audioTextController.handleAudioUpload(wavFile);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Test transcription", response.getBody());
    }

    @Test
    void testHandleAudioUpload_withNonWavFile() {
        MockMultipartFile mp3File = new MockMultipartFile(
                "file",
                "voice.mp3",
                "audio/mp3",
                "content".getBytes()
        );

        ResponseEntity<?> response = audioTextController.handleAudioUpload(mp3File);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Pls provide file in wav format !!", response.getBody());
    }

    @Test
    void testHandleAudioUpload_whenServiceThrowsException() throws Exception {
        MockMultipartFile wavFile = new MockMultipartFile(
                "file",
                "voice.wav",
                "audio/wav",
                "content".getBytes()
        );

        when(audioTextService.getTextFromAudio(any()))
                .thenThrow(new RuntimeException("Something went wrong"));

        ResponseEntity<?> response = audioTextController.handleAudioUpload(wavFile);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Some exception occurred !!", response.getBody());
    }
}
