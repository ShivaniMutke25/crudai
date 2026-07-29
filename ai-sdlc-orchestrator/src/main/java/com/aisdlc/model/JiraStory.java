package com.aisdlc.model;

import java.util.List;

public record JiraStory(

        String storyId,
        String title,
        String description,
        List<String> acceptanceCriteria

) {
}