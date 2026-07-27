package com.aisdlc.service;

import com.aisdlc.model.AffectedFile;
import com.aisdlc.model.RepositoryAnalysis;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RepositoryAnalysisValidator {

    private final Path repositoryRoot;

    private static final Set<String> VALID_CHANGE_TYPES =
            Set.of("MODIFY", "CREATE", "DELETE");

    public RepositoryAnalysisValidator() {
        this.repositoryRoot = Paths.get("..")
                .toAbsolutePath()
                .normalize();
    }

    public RepositoryAnalysis validate(
        RepositoryAnalysis analysis) {

    List<AffectedFile> verifiedFiles =
            new ArrayList<>();

    List<String> validationRisks =
            new ArrayList<>();

    if (analysis.affectedFiles() != null) {

        for (AffectedFile file : analysis.affectedFiles()) {

            // 1. Basic validation
            if (file == null || file.path() == null) {
                continue;
            }

            // 2. Normalize AI-generated change type
            String changeType =
                    normalizeChangeType(file.changeType());

            // 3. Resolve actual repository path
            Path resolvedPath =
                    repositoryRoot
                            .resolve(file.path())
                            .normalize();

            // 4. Security check
            if (!resolvedPath.startsWith(repositoryRoot)) {

                validationRisks.add(
                        "Rejected unsafe path: " + file.path()
                );

                continue;
            }

            // 5. Validate MODIFY / DELETE file exists
            if (changeType.equals("MODIFY")
                    || changeType.equals("DELETE")) {

                if (!Files.exists(resolvedPath)) {

                    validationRisks.add(
                            "AI suggested non-existing file: "
                                    + file.path()
                    );

                    continue;
                }
            }

            // 6. Validate CREATE file does NOT already exist
            if (changeType.equals("CREATE")
                    && Files.exists(resolvedPath)) {

                validationRisks.add(
                        "AI suggested CREATE for existing file: "
                                + file.path()
                );

                continue;
            }

            // 7. FIX NULL / EMPTY REASON  ← ADD IT HERE
            String reason = file.reason();

            if (reason == null || reason.isBlank()) {
                reason =
                        "File identified as impacted by repository analysis.";
            }

            // 8. Only now add the validated file
            verifiedFiles.add(
                    new AffectedFile(
                            file.path(),
                            reason,
                            changeType
                    )
            );
        }
    }

    // 9. Combine AI risks + validation risks
    List<String> combinedRisks =
            new ArrayList<>();

    if (analysis.risks() != null) {
        combinedRisks.addAll(analysis.risks());
    }

    combinedRisks.addAll(validationRisks);

    // 10. Return VERIFIED repository analysis
    return new RepositoryAnalysis(
            analysis.storyId(),
            verifiedFiles,
            analysis.backendFindings(),
            analysis.frontendFindings(),
            combinedRisks
    );
}

    private String normalizeChangeType(
            String changeType) {

        if (changeType == null) {
            return "MODIFY";
        }

        String normalized =
                changeType.trim().toUpperCase();

        if (VALID_CHANGE_TYPES.contains(normalized)) {
            return normalized;
        }

        /*
         * Our local model previously returned
         * Backend/Frontend instead of MODIFY.
         *
         * Existing affected files are modifications
         * unless explicitly identified otherwise.
         */
        if (normalized.equals("BACKEND")
                || normalized.equals("FRONTEND")
                || normalized.equals("DATABASE")
                || normalized.equals("TEST")) {

            return "MODIFY";
        }

        return "MODIFY";
    }
}