package com.epam.audioProcessor.controller;

import com.epam.audioProcessor.service.AudioTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * The type Audio upload controller.
 */
@RestController
public class AudioTextController {

    private AudioTextService audioTextService;

    /**
     * Sets audio text service.
     *
     * @param audioTextService the audio text service
     */
    @Autowired
    public void setAudioTextService(AudioTextService audioTextService) {
        this.audioTextService = audioTextService;
    }

    /**
     * Handle audio upload response entity.
     *
     * @param file the file
     * @return the response entity
     */
    @PostMapping("/upload-audio")
    public ResponseEntity<?> handleAudioUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (!isWav(file)) {
                return ResponseEntity
                        .badRequest()
                        .body("Pls provide file in wav format !!");
            }

            ResponseEntity<String> response = audioTextService.getTextFromAudio(file);
            return ResponseEntity.ok(response.getBody());

        } catch (Exception exception) {
            return ResponseEntity
                    .internalServerError()
                    .body("Some exception occurred !!");
        }
    }

    private boolean isWav(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename()).toLowerCase().endsWith(".wav");
    }

}
