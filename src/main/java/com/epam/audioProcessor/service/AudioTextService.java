package com.epam.audioProcessor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

/**
 * The type Audio text service.
 */
@Service
public class AudioTextService {

    @Value("${cloudFunctionUrl}")
    private final String cloudFunctionUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public AudioTextService(@Value("${cloudFunctionUrl}")
                            String cloudFunctionUrl, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.cloudFunctionUrl = cloudFunctionUrl;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    /**
     * Gets text from audio.
     *
     * @param file the file
     * @return the text from audio
     * @throws IOException the io exception
     */
    public ResponseEntity<String> getTextFromAudio(@NotNull MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String base64Audio = Base64.getEncoder().encodeToString(bytes);

        Map<String, String> payload = Map.of(
                "audio", base64Audio,
                "format", "LINEAR16"
        );


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = objectMapper.writeValueAsString(payload);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(cloudFunctionUrl, entity, String.class);

        return ResponseEntity.ok(response.getBody());
    }
}
