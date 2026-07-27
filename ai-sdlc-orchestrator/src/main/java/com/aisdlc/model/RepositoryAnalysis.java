package com.aisdlc.model;

import java.util.List;

public record RepositoryAnalysis(
        String storyId,
        List<AffectedFile> affectedFiles,
        List<String> backendFindings,
        List<String> frontendFindings,
        List<String> risks
) {
}