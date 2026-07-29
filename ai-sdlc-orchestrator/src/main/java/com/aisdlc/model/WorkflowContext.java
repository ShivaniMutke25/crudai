package com.aisdlc.model;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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