package com.aisdlc.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

@Component
public class RepositoryTools {

    private final Path repositoryRoot;

    public RepositoryTools() {

        this.repositoryRoot =
                Paths.get("..")
                        .toAbsolutePath()
                        .normalize();
    }

    @Tool(description = """
            List files in a repository directory.
            Use this to understand the repository structure.
            """)
    public String listFiles(String directory) {

        try {

            Path target = safePath(directory);

            if (!Files.exists(target)) {
                return "Directory does not exist: " + directory;
            }

            try (Stream<Path> paths = Files.walk(target, 4)) {

                return paths
                        .filter(Files::isRegularFile)
                        .filter(this::isAllowedFile)
                        .limit(200)
                        .map(repositoryRoot::relativize)
                        .map(Path::toString)
                        .reduce(
                                "",
                                (a, b) -> a + b + System.lineSeparator()
                        );
            }

        } catch (Exception e) {
            return "Unable to list files: " + e.getMessage();
        }
    }

    @Tool(description = """
            Read a source file from the repository.
            Use this after identifying a relevant file.
            """)
    public String readFile(String filePath) {

        try {

            Path target = safePath(filePath);

            if (!Files.exists(target)) {
                return "File does not exist: " + filePath;
            }

            if (!isAllowedFile(target)) {
                return "File type not allowed.";
            }

            long maxBytes = 50_000;

            if (Files.size(target) > maxBytes) {
                return "File is too large to read.";
            }

            return Files.readString(target);

        } catch (IOException e) {
            return "Unable to read file: " + e.getMessage();
        }
    }

    @Tool(description = """
            Search source files in the repository for a keyword.
            Use this to locate classes, components, API endpoints,
            fields or other relevant implementation details.
            """)
    public String searchCode(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return "Search keyword cannot be empty.";
        }

        StringBuilder result = new StringBuilder();

        try (Stream<Path> paths = Files.walk(repositoryRoot)) {

            paths
                    .filter(Files::isRegularFile)
                    .filter(this::isAllowedFile)
                    .filter(path -> !isIgnoredDirectory(path))
                    .forEach(path -> {

                        try {

                            int lineNumber = 0;

                            for (String line : Files.readAllLines(path)) {

                                lineNumber++;

                                if (line.toLowerCase()
                                        .contains(keyword.toLowerCase())) {

                                    result.append(
                                            repositoryRoot.relativize(path)
                                    );

                                    result.append(":");
                                    result.append(lineNumber);
                                    result.append(" -> ");
                                    result.append(line.trim());
                                    result.append(System.lineSeparator());

                                    if (result.length() > 20_000) {
                                        break;
                                    }
                                }
                            }

                        } catch (IOException ignored) {
                        }
                    });

        } catch (IOException e) {
            return "Search failed: " + e.getMessage();
        }

        if (result.isEmpty()) {
            return "No matches found for: " + keyword;
        }

        return result.toString();
    }

    private Path safePath(String requestedPath) {

        Path resolved =
                repositoryRoot.resolve(requestedPath)
                        .normalize();

        if (!resolved.startsWith(repositoryRoot)) {
            throw new IllegalArgumentException(
                    "Access outside repository is forbidden"
            );
        }

        return resolved;
    }

    private boolean isAllowedFile(Path path) {

        String name =
                path.getFileName()
                        .toString()
                        .toLowerCase();

        return name.endsWith(".java")
                || name.endsWith(".js")
                || name.endsWith(".jsx")
                || name.endsWith(".ts")
                || name.endsWith(".tsx")
                || name.endsWith(".json")
                || name.endsWith(".xml")
                || name.endsWith(".properties")
                || name.endsWith(".yml")
                || name.endsWith(".yaml");
    }

    private boolean isIgnoredDirectory(Path path) {

        String value = path.toString();

        return value.contains("node_modules")
                || value.contains("target")
                || value.contains(".git");
    }
}