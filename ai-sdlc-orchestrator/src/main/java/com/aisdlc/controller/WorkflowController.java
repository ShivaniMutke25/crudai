package com.aisdlc.controller;

import com.aisdlc.model.StoryAnalysis;
import com.aisdlc.service.AiSdlcOrchestrator;
import org.springframework.web.bind.annotation.*;
import com.aisdlc.model.RepositoryAnalysis;

@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {

    private final AiSdlcOrchestrator orchestrator;

    public WorkflowController(AiSdlcOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/analyze/{storyId}")
    public StoryAnalysis analyzeStory(@PathVariable String storyId) {
        return orchestrator.analyzeStory(storyId);
    }
    @PostMapping("/repository-analysis/{storyId}")
public RepositoryAnalysis analyzeRepository(
        @PathVariable String storyId) {

    return orchestrator.analyzeRepository(storyId);
}
}
