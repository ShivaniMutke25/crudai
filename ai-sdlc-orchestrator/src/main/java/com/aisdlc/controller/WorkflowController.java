package com.aisdlc.controller;

import com.aisdlc.model.StoryAnalysis;
import com.aisdlc.service.AiSdlcOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.aisdlc.model.RepositoryAnalysis;
import com.aisdlc.model.ImplementationPlan;
import com.aisdlc.model.ApprovalStatus;
import com.aisdlc.service.ApprovalService;

import java.util.Map;

@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {

    private static final Logger log = LoggerFactory.getLogger(WorkflowController.class);

    private final AiSdlcOrchestrator orchestrator;
    private final ApprovalService approvalService;

    public WorkflowController(AiSdlcOrchestrator orchestrator, ApprovalService approvalService) {
        this.orchestrator = orchestrator;
        this.approvalService = approvalService;
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
@PostMapping("/plan/{storyId}")
public ResponseEntity<?> createPlan(
        @PathVariable String storyId) {

    try {
        ImplementationPlan plan = orchestrator.createImplementationPlan(storyId);
        return ResponseEntity.ok(plan);
    } catch (IllegalStateException ex) {
        log.error("Plan creation failed for story {}", storyId, ex);
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
@PostMapping("/approve/{storyId}")
public ApprovalStatus approve(
        @PathVariable String storyId) {

    return approvalService.approve(storyId);
}
@PostMapping("/reject/{storyId}")
public ApprovalStatus reject(
        @PathVariable String storyId) {

    return approvalService.reject(storyId);
}
@GetMapping("/approval/{storyId}")
public ApprovalStatus approvalStatus(
        @PathVariable String storyId) {

    return approvalService.getStatus(storyId);
}
}
