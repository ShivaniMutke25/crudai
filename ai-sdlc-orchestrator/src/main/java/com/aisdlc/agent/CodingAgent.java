package com.aisdlc.agent;
import com.aisdlc.llm.LlmService;
import com.aisdlc.coding.SourceCodeWriter;
import com.aisdlc.coding.GitDiffGenerator;
import com.aisdlc.coding.model.CodeGenerationResult;
import com.aisdlc.model.ImplementationPlan;
import com.aisdlc.model.JiraStory;
import com.aisdlc.model.WorkflowContext;
import com.aisdlc.prompt.PromptLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
@Slf4j
public class CodingAgent implements Agent {

    private final PromptLoader promptLoader;

    private final LlmService llmService;

    private final SourceCodeWriter writer;

    private final GitDiffGenerator diffGenerator;

    @Override
    public void execute(WorkflowContext context){

        log.info("Coding Agent started.");

        String systemPrompt =
                promptLoader.loadAgent("coder.md");

        String userPrompt =        buildPrompt(context);

        CodeGenerationResult result =
                llmService.generate(

                        systemPrompt,

                        userPrompt,

                        CodeGenerationResult.class

                );

        result.changes()

                .forEach(writer::write);

        context.setGitDiff(

                diffGenerator.generate()

        );

        log.info("Coding Agent completed.");

    }

    private String buildPrompt(
            WorkflowContext context){

        return """
                Implementation Plan

                %s

                Repository Analysis

                %s
                """
                .formatted(

                        context.getImplementationPlan(),

                        context.getRepositoryAnalysis()

                );

    }

}