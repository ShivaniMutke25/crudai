package com.aisdlc.service;

import com.aisdlc.model.JiraStory;
import com.aisdlc.model.StoryAnalysis;
import com.aisdlc.model.RepositoryAnalysis;

import org.springframework.stereotype.Service;

@Service
public class AiSdlcOrchestrator {

    private final StoryReaderService storyReaderService;
    private final StoryAnalysisService storyAnalysisService;
    private final RepositoryAnalysisService repositoryAnalysisService;

    public AiSdlcOrchestrator(
            StoryReaderService storyReaderService,
            StoryAnalysisService storyAnalysisService,
            RepositoryAnalysisService repositoryAnalysisService) {

        this.storyReaderService = storyReaderService;
        this.storyAnalysisService = storyAnalysisService;
        this.repositoryAnalysisService = repositoryAnalysisService;
    }

    // Phase 4
    public StoryAnalysis analyzeStory(String storyId) {

        JiraStory story =
                storyReaderService.readStory(storyId);

        return storyAnalysisService.analyze(story);
    }

    // Phase 5
    public RepositoryAnalysis analyzeRepository(String storyId) {

        // Step 1: Read Jira story
        JiraStory story =
                storyReaderService.readStory(storyId);

        // Step 2: AI understands requirements
        StoryAnalysis storyAnalysis =
                storyAnalysisService.analyze(story);

        // Step 3: AI analyzes actual repository
        return repositoryAnalysisService.analyze(
                story,
                storyAnalysis
        );
    }
}