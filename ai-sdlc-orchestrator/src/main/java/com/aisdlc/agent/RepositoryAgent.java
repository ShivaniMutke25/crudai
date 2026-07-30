package com.aisdlc.agent;

import com.aisdlc.llm.LlmService;
import com.aisdlc.model.ImplementationPlan;
import com.aisdlc.model.RepositoryAnalysis;
import com.aisdlc.model.WorkflowContext;
import com.aisdlc.prompt.PromptLoader;
import com.aisdlc.repository.RepositoryContextBuilder;
import com.aisdlc.repository.RepositoryScanner;
import com.aisdlc.repository.RepositorySearcher;
import com.aisdlc.repository.RepositoryContext;
import com.aisdlc.repository.RepositoryMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepositoryAgent implements Agent {

    private final RepositoryScanner repositoryScanner;

    private final RepositorySearcher repositorySearcher;

    private final RepositoryContextBuilder repositoryContextBuilder;

    private final PromptLoader promptLoader;

    private final LlmService llmService;

    @Override
    public void execute(WorkflowContext context) {

        log.info("Repository Agent started.");

        ImplementationPlan plan = context.getImplementationPlan();

        validate(plan);

        List<RepositoryMetadata> repositoryIndex =
                repositoryScanner.scan(Paths.get("."));

        List<RepositoryMetadata> relevantFiles =
                repositorySearcher.search(
                        repositoryIndex,
                        plan.searchKeywords());

        RepositoryContext repositoryContext =
                repositoryContextBuilder.build(relevantFiles);

        String systemPrompt =
                promptLoader.loadAgent("repository.md");

        String userPrompt =
                buildUserPrompt(plan, repositoryContext);

        // RepositoryAnalysis analysis =
        //         llmService.generate(

        //                 systemPrompt,

        //                 userPrompt,

        //                 RepositoryAnalysis.class

        //         );

       RepositoryAnalysis analysis = new RepositoryAnalysis(
    "Repository scanned successfully",
    List.of()
);


        context.setRepositoryAnalysis(analysis);

        log.info("Repository Agent completed successfully.");

    }

    private void validate(ImplementationPlan plan) {

        if (plan == null) {

            throw new IllegalStateException(
                    "Implementation Plan is missing.");

        }

        if (plan.searchKeywords() == null
                || plan.searchKeywords().isEmpty()) {

            throw new IllegalStateException(
                    "Search keywords are missing.");

        }

    }

    private String buildUserPrompt(

            ImplementationPlan plan,

            RepositoryContext repositoryContext) {

        StringBuilder builder = new StringBuilder();

        builder.append("Implementation Plan\n");

        builder.append(plan);

        builder.append("\n\n");

        builder.append("Repository Context\n");

        repositoryContext.files()

                .forEach(file -> {

                    builder.append("=================================\n");

                    builder.append(file.path());

                    builder.append("\n");

                    builder.append("---------------------------------\n");

                    builder.append(file.content());

                    builder.append("\n\n");

                });

        return builder.toString();

    }

}