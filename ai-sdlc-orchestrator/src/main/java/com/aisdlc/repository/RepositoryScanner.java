package com.aisdlc.repository;

import com.aisdlc.repository.RepositoryMetadata;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepositoryScanner {

    public List<RepositoryMetadata> scan(Path root) {

        List<RepositoryMetadata> files = new ArrayList<>();

        try {

            Files.walk(root)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {

                        String name = path.getFileName().toString();

                        if (supported(name)) {

                            files.add(
                                    RepositoryMetadata.builder()
                                            .path(path.toString())
                                            .fileName(name)
                                            .type(detectType(name))
                                            .build()
                            );

                        }

                    });

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

        return files;

    }

    private boolean supported(String name) {

        return name.endsWith(".java")
                || name.endsWith(".jsx")
                || name.endsWith(".tsx")
                || name.endsWith(".sql");

    }

    private String detectType(String name) {

        if (name.contains("Controller"))
            return "Controller";

        if (name.contains("Service"))
            return "Service";

        if (name.contains("Repository"))
            return "Repository";

        if (name.contains("Entity"))
            return "Entity";

        if (name.endsWith(".jsx"))
            return "React";

        return "Other";

    }

}