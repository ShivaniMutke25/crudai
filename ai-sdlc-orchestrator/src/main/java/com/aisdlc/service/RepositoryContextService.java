package com.aisdlc.service;

import com.aisdlc.model.JiraStory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

@Service
public class RepositoryContextService {

    private final Path repositoryRoot;

    // Keep context small because Ollama is local
    private static final int MAX_FILES = 6;
    private static final int MAX_FILE_CHARS = 8000;
    private static final int MAX_TOTAL_CHARS = 25000;

    public RepositoryContextService() {

        /*
         * ai-sdlc-orchestrator is inside crud-employee.
         *
         * Example:
         *
         * crud-employee/
         *   ai-sdlc-orchestrator/
         *   springboot-backend/
         *   react-frontend/
         *
         * Therefore ".." points to crud-employee.
         */
        this.repositoryRoot = Paths.get("..")
                .toAbsolutePath()
                .normalize();
    }

    public String buildContext(JiraStory story) {

        Set<String> keywords = extractKeywords(story);

        List<Path> candidateFiles = new ArrayList<>();

        // Scan backend
        scanDirectory(
                repositoryRoot.resolve("springboot-backend/src"),
                keywords,
                candidateFiles
        );

        // Scan frontend
        scanDirectory(
                repositoryRoot.resolve("react-frontend/src"),
                keywords,
                candidateFiles
        );

        // Rank most relevant files first
        candidateFiles.sort(
                Comparator.comparingInt(
                        path -> -scoreFile(path, keywords)
                )
        );

        // Debugging - useful during development
        System.out.println("Repository root: " + repositoryRoot);

        System.out.println(
                "Repository keywords: " + keywords
        );

        candidateFiles.stream()
                .limit(MAX_FILES)
                .forEach(path ->
                        System.out.println(
                                "Selected repository file: "
                                        + repositoryRoot.relativize(path)
                        )
                );

        StringBuilder context = new StringBuilder();

        candidateFiles.stream()
                .limit(MAX_FILES)
                .forEach(path ->
                        appendFile(context, path)
                );

        System.out.println(
                "Repository context size: "
                        + context.length()
                        + " characters"
        );

        if (context.isEmpty()) {
            return "No relevant repository files were discovered.";
        }

        return context.toString();
    }

    private void scanDirectory(
            Path directory,
            Set<String> keywords,
            List<Path> results) {

        if (!Files.exists(directory)) {

            System.out.println(
                    "Repository directory not found: "
                            + directory
            );

            return;
        }

        try (Stream<Path> paths = Files.walk(directory)) {

            paths
                    .filter(Files::isRegularFile)
                    .filter(this::isSourceFile)
                    .filter(path ->
                            scoreFile(path, keywords) > 0
                    )
                    .forEach(results::add);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to scan repository: "
                            + directory,
                    e
            );
        }
    }

    private int scoreFile(
            Path path,
            Set<String> keywords) {

        int score = 0;

        String fileName =
                path.getFileName()
                        .toString()
                        .toLowerCase();

        // Filename matches get higher score
        for (String keyword : keywords) {

            if (fileName.contains(keyword)) {
                score += 10;
            }
        }

        // Content matches get smaller score
        try {

            String content =
                    Files.readString(path)
                            .toLowerCase();

            for (String keyword : keywords) {

                if (content.contains(keyword)) {
                    score += 2;
                }
            }

        } catch (IOException ignored) {
        }

        return score;
    }

    private void appendFile(
            StringBuilder context,
            Path path) {

        if (context.length() >= MAX_TOTAL_CHARS) {
            return;
        }

        try {

            String content = Files.readString(path);

            // Prevent huge files from being sent to Ollama
            if (content.length() > MAX_FILE_CHARS) {

                content =
                        content.substring(
                                0,
                                MAX_FILE_CHARS
                        );
            }

            String relativePath =
                    repositoryRoot
                            .relativize(path)
                            .toString()
                            .replace("\\", "/");

            String fileContext = """

                    ========================================
                    FILE: %s
                    ========================================

                    %s

                    """.formatted(
                    relativePath,
                    content
            );

            int remaining =
                    MAX_TOTAL_CHARS - context.length();

            if (fileContext.length() > remaining) {

                fileContext =
                        fileContext.substring(
                                0,
                                remaining
                        );
            }

            context.append(fileContext);

        } catch (IOException ignored) {
        }
    }

    private Set<String> extractKeywords(
            JiraStory story) {

        Set<String> keywords =
                new LinkedHashSet<>();

        String combined =
                (
                        story.title()
                                + " "
                                + story.description()
                                + " "
                                + story.acceptanceCriteria()
                ).toLowerCase();

        /*
         * Simple lexical retrieval for our POC.
         *
         * Later:
         * embeddings / RAG / hybrid search can replace this.
         */
        List<String> importantTerms =
                List.of(
                        "employee",
                        "department",
                        "create",
                        "update",
                        "edit",
                        "list"
                );

        for (String term : importantTerms) {

            if (combined.contains(term)) {
                keywords.add(term);
            }
        }

        return keywords;
    }

    private boolean isSourceFile(Path path) {

        String name =
                path.getFileName()
                        .toString()
                        .toLowerCase();

        return name.endsWith(".java")
                || name.endsWith(".js")
                || name.endsWith(".jsx")
                || name.endsWith(".ts")
                || name.endsWith(".tsx");
    }
}