package com.aisdlc.service;

import com.aisdlc.model.JiraStory;
import com.aisdlc.model.RepositoryAnalysis;
import com.aisdlc.model.StoryAnalysis;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RepositoryAnalysisService {

    private final ChatClient chatClient;

    // Java service that retrieves relevant repository files
    private final RepositoryContextService repositoryContextService;

    // Java validator that checks AI output
    private final RepositoryAnalysisValidator validator;

    public RepositoryAnalysisService(
            ChatClient.Builder builder,
            RepositoryContextService repositoryContextService,
            RepositoryAnalysisValidator validator) {

        this.chatClient = builder.build();
        this.repositoryContextService = repositoryContextService;
        this.validator = validator;
    }

    public RepositoryAnalysis analyze(
            JiraStory story,
            StoryAnalysis storyAnalysis) {

        // STEP 1:
        // Deterministically retrieve relevant repository files.
        String repositoryContext =
                repositoryContextService.buildContext(story);

        // STEP 2:
        // Give actual repository context to the LLM.
        RepositoryAnalysis aiAnalysis =
                chatClient
                        .prompt()
                        .system("""
                            You are a senior full-stack engineer
                            performing repository impact analysis.

                            You receive source files retrieved from
                            the REAL repository.

                            Determine which EXISTING files are
                            affected by the development story.

                            STRICT RULES:

                            1. Never invent file paths.

                            2. Only return files shown in
                               VERIFIED REPOSITORY CONTEXT.

                            3. Copy file paths exactly as provided.

                            4. Do not shorten or modify paths.

                            5. Do not create package names.

                            6. This phase only identifies existing
                               files that require modification.

                            7. changeType MUST always be:
                               MODIFY

                            8. Do NOT use Backend, Frontend,
                               Database or Test as changeType.

                            9. Explain why each affected file
                               needs modification.

                            10. Do not modify source code.
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

                            Summary:
                            %s

                            Backend changes required:
                            %s

                            Frontend changes required:
                            %s

                            Database changes required:
                            %s

                            Requirements:
                            %s


                            =========================
                            VERIFIED REPOSITORY CONTEXT
                            =========================

                            %s


                            Analyze repository impact.

                            Return ONLY existing files contained
                            in VERIFIED REPOSITORY CONTEXT.
                            """.formatted(
                                story.key(),
                                story.title(),
                                story.description(),
                                story.acceptanceCriteria(),

                                storyAnalysis.summary(),
                                storyAnalysis.backendChangesRequired(),
                                storyAnalysis.frontendChangesRequired(),
                                storyAnalysis.databaseChangesRequired(),
                                storyAnalysis.requirements(),

                                repositoryContext
                        ))
                        .call()
                        .entity(RepositoryAnalysis.class);

        // STEP 3:
        // Never trust LLM output directly.
        return validator.validate(aiAnalysis);
    }
}