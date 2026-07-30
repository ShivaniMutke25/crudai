package com.aisdlc.llm;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class LlmService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public LlmService(ChatClient.Builder builder,
                      ObjectMapper objectMapper) {
        this.chatClient = builder.build();
        this.objectMapper = objectMapper;
    }

    public <T> T generate(String systemPrompt,
                          String userPrompt,
                          Class<T> clazz) {

        try {

            String response = chatClient.prompt()
                    .system(systemPrompt)
                    .user(userPrompt)
                    .call()
                    .content();

            System.out.println("===== OLLAMA RESPONSE =====");
            System.out.println(response);

            return objectMapper.readValue(response, clazz);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate response from Ollama", e);
        }
    }
}