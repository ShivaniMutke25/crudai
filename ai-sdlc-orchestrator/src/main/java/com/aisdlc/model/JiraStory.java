package com.aisdlc.model;

import java.util.List;

public record JiraStory(
        String key,
        String title,
        String description,
        List<String> acceptanceCriteria,
        String priority
) {
}
