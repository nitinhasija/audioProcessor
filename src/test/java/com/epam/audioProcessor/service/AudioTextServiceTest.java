package com.epam.audioProcessor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AudioTextServiceTest {

    private RestTemplate restTemplate;
    private AudioTextService audioTextService;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        ObjectMapper objectMapper = new ObjectMapper();
        audioTextService = new AudioTextService("https://dummy-url", restTemplate, objectMapper);
    }

    @Test
    void testGetTextFromAudio_success() throws Exception {
        byte[] audioBytes = "some audio".getBytes();
        MultipartFile mockFile = new MockMultipartFile("file", "audio.wav", "audio/wav", audioBytes);
        String expectedResponse = "Text here";
        ResponseEntity<String> mockedResponse = ResponseEntity.ok(expectedResponse);

        when(restTemplate.postForEntity(
                eq("https://dummy-url"),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockedResponse);

        ResponseEntity<String> response = audioTextService.getTextFromAudio(mockFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testGetTextFromAudio_whenRestCallFails_shouldThrow() throws Exception {
        MultipartFile mockFile = new MockMultipartFile("file", "audio.wav", "audio/wav", "data".getBytes());

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new RuntimeException("API error"));

        assertThrows(RuntimeException.class, () -> {
            audioTextService.getTextFromAudio(mockFile);
        });
    }
}

