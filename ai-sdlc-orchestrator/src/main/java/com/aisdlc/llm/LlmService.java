package com.aisdlc.llm;

import org.springframework.stereotype.Service;

@Service
public class LlmService {

    public <T> T generate(String systemPrompt,
                          String userPrompt,
                          Class<T> clazz) {

        throw new UnsupportedOperationException(
                "LLM integration is not implemented in this prototype.");
    }
}