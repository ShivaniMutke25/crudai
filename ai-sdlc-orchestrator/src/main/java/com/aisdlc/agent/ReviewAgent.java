package com.aisdlc.agent;

import com.aisdlc.llm.LlmService;
import com.aisdlc.model.ReviewResult;
import com.aisdlc.model.WorkflowContext;
import com.aisdlc.prompt.PromptLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewAgent implements Agent {

    private final PromptLoader promptLoader;

    private final LlmService llmService;

    @Override
    public void execute(WorkflowContext context) {

        log.info("Review Agent Started");

        validate(context);

        String systemPrompt =
                promptLoader.loadAgent("reviewer.md");

        String userPrompt =
                buildPrompt(context);

        ReviewResult result =
                llmService.generate(
                        systemPrompt,
                        userPrompt,
                        ReviewResult.class);

        context.setReviewResult(result);

        log.info("Review Agent Completed");

    }

    private void validate(WorkflowContext context) {

        if (context.getGitDiff() == null) {

            throw new IllegalStateException("Git Diff missing");

        }

        if (context.getTestResult() == null) {

            throw new IllegalStateException("Test Result missing");

        }

    }

    private String buildPrompt(WorkflowContext context) {

        return """
                Implementation Plan

                %s

                Git Diff

                %s

                Test Result

                %s
                """
                .formatted(
                        context.getImplementationPlan(),
                        context.getGitDiff(),
                        context.getTestResult());

    }

}