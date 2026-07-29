package com.aisdlc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowContext {

    private JiraStory jiraStory;

    private ImplementationPlan implementationPlan;

    private RepositoryAnalysis repositoryAnalysis;

    private GitDiff gitDiff;

    private TestResult testResult;

    private ReviewResult reviewResult;

    private PullRequestResult pullRequestResult;

}