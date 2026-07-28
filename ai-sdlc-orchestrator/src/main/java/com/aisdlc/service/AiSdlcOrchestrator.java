package com.aisdlc.service;

import com.aisdlc.model.ApprovalStatus;
import com.aisdlc.model.ImplementationPlan;
import com.aisdlc.model.JiraStory;
import com.aisdlc.model.RepositoryAnalysis;
import com.aisdlc.model.StoryAnalysis;
import org.springframework.stereotype.Service;

@Service
public class AiSdlcOrchestrator {

    private final StoryReaderService storyReaderService;
    private final StoryAnalysisService storyAnalysisService;
    private final RepositoryAnalysisService repositoryAnalysisService;
    private final ImplementationPlanningService implementationPlanningService;
    private final ImplementationPlanValidator implementationPlanValidator;
    private final WorkflowStateService workflowStateService;
    private final ApprovalService approvalService;

    public AiSdlcOrchestrator(
            StoryReaderService storyReaderService,
            StoryAnalysisService storyAnalysisService,
            RepositoryAnalysisService repositoryAnalysisService,
            ImplementationPlanningService implementationPlanningService,
            ImplementationPlanValidator implementationPlanValidator,
            WorkflowStateService workflowStateService,
            ApprovalService approvalService) {

        this.storyReaderService = storyReaderService;
        this.storyAnalysisService = storyAnalysisService;
        this.repositoryAnalysisService = repositoryAnalysisService;
        this.implementationPlanningService = implementationPlanningService;
        this.implementationPlanValidator = implementationPlanValidator;
        this.workflowStateService = workflowStateService;
        this.approvalService = approvalService;
    }

    /**
     * ==========================
     * Phase 4
     * Story Analysis
     * ==========================
     */
    public StoryAnalysis analyzeStory(String storyId){

        JiraStory story = storyReaderService.readStory(storyId);

        StoryAnalysis storyAnalysis =
                storyAnalysisService.analyze(story);

        workflowStateService.saveStoryAnalysis(
                storyId,
                storyAnalysis
        );

        return storyAnalysis;
    }

    /**
     * ==========================
     * Phase 5
     * Repository Analysis
     * ==========================
     */
    public RepositoryAnalysis analyzeRepository(String storyId) {

        JiraStory story =
                storyReaderService.readStory(storyId);

        StoryAnalysis storyAnalysis =
                workflowStateService.loadStoryAnalysis(storyId);

        RepositoryAnalysis repositoryAnalysis =
                repositoryAnalysisService.analyze(
                        story,
                        storyAnalysis
                );

        workflowStateService.saveRepositoryAnalysis(
                storyId,
                repositoryAnalysis
        );

        return repositoryAnalysis;
    }

    /**
     * ==========================
     * Phase 6
     * Implementation Planning
     * ==========================
     */
    public ImplementationPlan createImplementationPlan(
            String storyId) {

        JiraStory story =
                storyReaderService.readStory(storyId);

        StoryAnalysis storyAnalysis =
                workflowStateService.loadStoryAnalysis(storyId);

        RepositoryAnalysis repositoryAnalysis =
                workflowStateService.loadRepositoryAnalysis(storyId);

        ImplementationPlan aiPlan =
                implementationPlanningService.createPlan(
                        story,
                        storyAnalysis,
                        repositoryAnalysis
                );

        ImplementationPlan validatedPlan =
                implementationPlanValidator.validate(aiPlan);

        workflowStateService.saveImplementationPlan(
                storyId,
                validatedPlan,
                ApprovalStatus.PENDING
        );

        approvalService.createPending(storyId);

        return validatedPlan;
    }
}