package com.aisdlc.service;

import com.aisdlc.model.ImplementationPlan;
import com.aisdlc.model.JiraStory;
import com.aisdlc.model.RepositoryAnalysis;
import com.aisdlc.model.StoryAnalysis;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ImplementationPlanningService {

    private final ChatClient chatClient;
    private final RepositoryContextService repositoryContextService;

    public ImplementationPlanningService(
            ChatClient.Builder builder,
            RepositoryContextService repositoryContextService) {

        this.chatClient = builder.build();
        this.repositoryContextService = repositoryContextService;
    }

    public ImplementationPlan createPlan(
            JiraStory story,
            StoryAnalysis storyAnalysis,
            RepositoryAnalysis repositoryAnalysis) {

        // Retrieve real repository evidence again
        String repositoryContext =
                repositoryContextService.buildContext(story);

        return chatClient
                .prompt()
                .system("""
                    You are a senior full-stack engineer creating
                    an implementation plan.

                    You MUST NOT modify source code.

                    Create a safe and ordered implementation plan
                    based on the development story and repository.

                    RULES:

                    1. Keep changes limited to the story requirements.

                    2. For existing files, use exact paths from
                       the repository context.

                    3. Never invent an existing file path.

                    4. Existing files should use action MODIFY.

                    5. CREATE may only be used when a genuinely
                       new file is required.

                    6. Valid actions are:
                       MODIFY
                       CREATE
                       DELETE

                    7. Valid layers are:
                       BACKEND
                       FRONTEND
                       DATABASE
                       TEST

                    8. Include a test strategy.

                    9. Identify implementation risks.

                    10. requiresHumanApproval MUST be true.

                    11. Do NOT generate implementation code.

                    12. Do NOT modify repository files.
                    """)
                .user("""
                    =========================
                    DEVELOPMENT STORY
                    =========================

                    Story ID:
                    %s

                    Title:
                    %s

                    Description:
                    %s

                    Acceptance Criteria:
                    %s


                    =========================
                    STORY ANALYSIS
                    =========================

                    %s


                    =========================
                    VERIFIED REPOSITORY ANALYSIS
                    =========================

                    %s


                    =========================
                    ACTUAL REPOSITORY CONTEXT
                    =========================

                    %s


                    Create an ordered implementation plan.
                    """.formatted(
                        story.key(),
                        story.title(),
                        story.description(),
                        story.acceptanceCriteria(),
                        storyAnalysis,
                        repositoryAnalysis,
                        repositoryContext
                ))
                .call()
                .entity(ImplementationPlan.class);
    }
}