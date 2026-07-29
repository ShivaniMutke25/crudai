package com.aisdlc.agent;

import com.aisdlc.llm.LlmService;
import com.aisdlc.model.ImplementationPlan;
import com.aisdlc.model.JiraStory;
import com.aisdlc.model.WorkflowContext;
import com.aisdlc.prompt.PromptLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlannerAgent implements Agent {

    private final PromptLoader promptLoader;
    private final LlmService llmService;

    @Override
    public void execute(WorkflowContext context) {

        log.info("Planner Agent Started");

        JiraStory story = context.getJiraStory();

        validate(story);

        String systemPrompt =
                promptLoader.loadAgent("planner.md");

        String userPrompt = buildPrompt(story);

        ImplementationPlan plan =
                llmService.generate(
                        systemPrompt,
                        userPrompt,
                        ImplementationPlan.class);

        validate(plan);

        context.setImplementationPlan(plan);

        log.info("Planner Agent Completed");

    }

    private void validate(JiraStory story) {

        if (story == null) {
            throw new IllegalArgumentException("Jira Story is missing");
        }

    }

    private void validate(ImplementationPlan plan) {

        if (plan == null) {
            throw new IllegalStateException("Implementation Plan generation failed");
        }

        if (!plan.requiresHumanApproval()) {
            throw new IllegalStateException("Plan must require human approval");
        }

    }

    private String buildPrompt(JiraStory story) {

        return """
                Story ID:
                %s

                Title:
                %s

                Description:
                %s

                Acceptance Criteria:
                %s
                """
                .formatted(
                        story.storyId(),
                        story.title(),
                        story.description(),
                        story.acceptanceCriteria());

    }

}