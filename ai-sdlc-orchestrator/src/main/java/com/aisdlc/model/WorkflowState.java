package com.aisdlc.model;

public record WorkflowState(

        JiraStory story,

        StoryAnalysis storyAnalysis,

        RepositoryAnalysis repositoryAnalysis,

        ImplementationPlan implementationPlan,

        ApprovalStatus approvalStatus

) {}