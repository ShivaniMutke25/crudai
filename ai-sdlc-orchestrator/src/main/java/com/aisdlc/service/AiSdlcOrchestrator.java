package com.aisdlc.service;

import com.aisdlc.model.JiraStory;
import com.aisdlc.model.StoryAnalysis;
import org.springframework.stereotype.Service;

@Service
public class AiSdlcOrchestrator {

    private final StoryReaderService storyReaderService;
    private final StoryAnalysisService storyAnalysisService;

    public AiSdlcOrchestrator(
            StoryReaderService storyReaderService,
            StoryAnalysisService storyAnalysisService) {

        this.storyReaderService = storyReaderService;
        this.storyAnalysisService = storyAnalysisService;
    }

    public StoryAnalysis analyzeStory(String storyId) {
        JiraStory story = storyReaderService.readStory(storyId);
        return storyAnalysisService.analyze(story);
    }
}
