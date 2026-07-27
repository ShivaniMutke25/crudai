package com.aisdlc.service;

import com.aisdlc.model.JiraStory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoryReaderServiceTest {

    @Test
    void readsStoryFromStoriesFolder() {
        StoryReaderService service = new StoryReaderService(new ObjectMapper());
        JiraStory story = service.readStory("EMP-101");

        assertEquals("EMP-101", story.key());
        assertEquals("Add department to employee", story.title());
    }
}
