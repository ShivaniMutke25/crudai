package com.aisdlc.model;

import java.util.List;

public record StoryAnalysis(
        String storyId,
        String summary,
        boolean backendChangesRequired,
        boolean frontendChangesRequired,
        boolean databaseChangesRequired,
        String risk,
        List<String> requirements
) {
}
