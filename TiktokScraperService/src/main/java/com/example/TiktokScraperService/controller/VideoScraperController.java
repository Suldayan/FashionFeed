package com.example.TiktokScraperService.controller;

import com.example.TiktokScraperService.customErrorHandling.VideoScraperException;
import com.example.TiktokScraperService.service.VideoScraperService;
import com.microsoft.playwright.ElementHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController("/api/vs")
public class VideoScraperController {

    private static final Logger logger = LoggerFactory.getLogger(VideoScraperController.class);

    private final VideoScraperService videoScraperService;

    public VideoScraperController(VideoScraperService videoScraperService) {
        this.videoScraperService = videoScraperService;
    }

    @GetMapping("/elements")
    public Optional<ElementHandle[]> obtainAllVideoElements() throws VideoScraperException {
        logger.info("Rest Controller has been accessed for all video elements at: {}", LocalDateTime.now());

        return videoScraperService.getAllVideoElementsOnCurrentDom();
    }
}
