package com.aisdlc.agent;

import com.aisdlc.github.GitHubService;
import com.aisdlc.llm.LlmService;
import com.aisdlc.model.PullRequestResult;
import com.aisdlc.model.WorkflowContext;
import com.aisdlc.prompt.PromptLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PullRequestAgent implements Agent {

    private final PromptLoader promptLoader;

    private final LlmService llmService;

    private final GitHubService gitHubService;

    @Override
    public void execute(WorkflowContext context) {

        log.info("Pull Request Agent Started");

        validate(context);

        String systemPrompt =
                promptLoader.loadAgent("pr.md");

        String userPrompt =
                buildPrompt(context);

        PullRequestResult llmResult =
                llmService.generate(
                        systemPrompt,
                        userPrompt,
                        PullRequestResult.class);

        PullRequestResult result =
                gitHubService.createPullRequest(
                        llmResult.getStatus(),
                        llmResult.getUrl());

        context.setPullRequestResult(result);

        log.info("Pull Request Agent Completed");

    }

    private void validate(WorkflowContext context) {

        if (context.getReviewResult() == null) {

            throw new IllegalStateException(
                    "Review Result missing");

        }

        if (!context.getReviewResult().isApproved()) {

            throw new IllegalStateException(
                    "Review not approved");

        }

    }

    private String buildPrompt(WorkflowContext context) {

        return """
                Implementation Plan

                %s

                Review Summary

                %s

                Git Diff

                %s
                """
                .formatted(
                        context.getImplementationPlan(),
                        context.getReviewResult().getSummary(),
                        context.getGitDiff());

    }

}