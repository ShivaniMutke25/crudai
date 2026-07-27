package com.aisdlc.service;

import com.aisdlc.model.JiraStory;
import com.aisdlc.model.StoryAnalysis;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class StoryAnalysisService {

    private final ChatClient chatClient;

    public StoryAnalysisService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public StoryAnalysis analyze(JiraStory story) {
        return chatClient
                .prompt()
                .system("""
                    You are a senior software engineer performing
                    requirement analysis for an AI-native SDLC system.

                    Analyze the provided development story.

                    Determine:
                    - concise technical summary
                    - whether backend changes are required
                    - whether frontend changes are required
                    - whether database changes are required
                    - implementation risk: LOW, MEDIUM or HIGH
                    - normalized technical requirements

                    Do not invent requirements that are not supported
                    by the story.
                    """)
                .user("""
                    Story ID: %s

                    Title:
                    %s

                    Description:
                    %s

                    Acceptance Criteria:
                    %s

                    Priority:
                    %s
                    """.formatted(
                        story.key(),
                        story.title(),
                        story.description(),
                        story.acceptanceCriteria(),
                        story.priority()
                ))
                .call()
                .entity(StoryAnalysis.class);
    }
}
