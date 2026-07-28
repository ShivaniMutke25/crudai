package com.aisdlc.service;

import com.aisdlc.model.JiraStory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
public class RepositoryContextService {

    private final Path repositoryRoot;

    // Optimized for local Ollama
    private static final int MAX_FILES = 3;
    private static final int MAX_FILE_CHARS = 2500;
    private static final int MAX_TOTAL_CHARS = 8000;

    // Cache repository context per story
    private final Map<String, String> contextCache =
            new ConcurrentHashMap<>();

    public RepositoryContextService() {

        this.repositoryRoot = Paths.get("..")
                .toAbsolutePath()
                .normalize();
    }

    /**
     * Public API
     */
    public String buildContext(JiraStory story) {

        return contextCache.computeIfAbsent(
                story.key(),
                key -> buildContextInternal(story)
        );
    }

    /**
     * Clear cache (optional)
     */
    public void clearCache() {
        contextCache.clear();
    }

    /**
     * Actual repository scan
     */
    private String buildContextInternal(JiraStory story) {

        Set<String> keywords = extractKeywords(story);

        List<RepositoryFile> files = new ArrayList<>();

        scanDirectory(
                repositoryRoot.resolve("springboot-backend/src"),
                keywords,
                files
        );

        scanDirectory(
                repositoryRoot.resolve("react-frontend/src"),
                keywords,
                files
        );

        files.sort(
                Comparator.comparingInt(RepositoryFile::score)
                        .reversed()
        );

        StringBuilder context = new StringBuilder();

        for (RepositoryFile file : files) {

            if (context.length() >= MAX_TOTAL_CHARS) {
                break;
            }

            appendFile(context, file);
        }

        if (context.isEmpty()) {
            return "No relevant repository files found.";
        }

        return context.toString();
    }

    /**
     * Scan repository
     */
    private void scanDirectory(
            Path directory,
            Set<String> keywords,
            List<RepositoryFile> results) {

        if (!Files.exists(directory)) {
            return;
        }

        try (Stream<Path> paths = Files.walk(directory)) {

            paths.filter(Files::isRegularFile)
                    .filter(this::isSourceFile)
                    .forEach(path -> {

                        try {

                            String content =
                                    Files.readString(path);

                            int score =
                                    scoreFile(
                                            path,
                                            content,
                                            keywords);

                            if (score > 0) {

                                if (content.length() >
                                        MAX_FILE_CHARS) {

                                    content =
                                            content.substring(
                                                    0,
                                                    MAX_FILE_CHARS);
                                }

                                results.add(
                                        new RepositoryFile(
                                                path,
                                                content,
                                                score
                                        )
                                );
                            }

                        } catch (IOException ignored) {
                        }

                    });

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to scan repository",
                    e
            );
        }
    }

    /**
     * Better scoring
     */
    private int scoreFile(
            Path path,
            String content,
            Set<String> keywords) {

        int score = 0;

        String fileName =
                path.getFileName()
                        .toString()
                        .toLowerCase();

        String lowerContent =
                content.toLowerCase();

        for (String keyword : keywords) {

            if (fileName.contains(keyword)) {
                score += 10;
            }

            score += countOccurrences(
                    lowerContent,
                    keyword
            );
        }

        return score;
    }

    /**
     * Count keyword frequency
     */
    private int countOccurrences(
            String text,
            String keyword) {

        int count = 0;
        int index = 0;

        while ((index =
                text.indexOf(keyword, index)) != -1) {

            count++;
            index += keyword.length();
        }

        return count;
    }

    /**
     * Build prompt context
     */
    private void appendFile(
            StringBuilder context,
            RepositoryFile file) {

        String relativePath =
                repositoryRoot
                        .relativize(file.path())
                        .toString()
                        .replace("\\", "/");

        String fileContext = """

                ========================================
                FILE: %s
                ========================================

                %s

                """.formatted(
                relativePath,
                file.content()
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
    }

    /**
     * Extract important story keywords
     */
    private Set<String> extractKeywords(
            JiraStory story) {

        Set<String> keywords =
                new LinkedHashSet<>();

        String text =
                (
                        story.title()
                                + " "
                                + story.description()
                                + " "
                                + story.acceptanceCriteria()
                ).toLowerCase();

        List<String> importantTerms =
                List.of(
                        "employee",
                        "department",
                        "create",
                        "update",
                        "edit",
                        "delete",
                        "list",
                        "controller",
                        "service",
                        "repository",
                        "model",
                        "entity"
                );

        for (String term : importantTerms) {

            if (text.contains(term)) {
                keywords.add(term);
            }
        }

        return keywords;
    }

    /**
     * Supported source files
     */
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

    /**
     * Internal helper
     */
    private record RepositoryFile(
            Path path,
            String content,
            int score
    ) {
    }
}