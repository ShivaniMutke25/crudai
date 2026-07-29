package com.aisdlc.prompt;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class PromptLoader {

    public String loadAgent(String agentFile) {

        return read(".github/agents/" + agentFile);

    }

    public String loadPrompt(String promptFile) {

        return read(".github/prompts/" + promptFile);

    }

    private String read(String path) {

        try {

            return Files.readString(Path.of(path));

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to load prompt : " + path,
                    e);

        }

    }

}