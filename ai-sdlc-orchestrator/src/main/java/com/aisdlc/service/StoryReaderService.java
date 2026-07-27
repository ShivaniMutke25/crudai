package com.aisdlc.service;

import com.aisdlc.model.JiraStory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class StoryReaderService {

    private final ObjectMapper objectMapper;

    public StoryReaderService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JiraStory readStory(String storyId) {
        try {
            File file = new File("../stories/" + storyId + ".json");
            return objectMapper.readValue(file, JiraStory.class);
        } catch (Exception e) {
            throw new RuntimeException("Unable to read story: " + storyId, e);
        }
    }
}
