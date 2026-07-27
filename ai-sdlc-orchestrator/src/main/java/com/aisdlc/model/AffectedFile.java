package com.aisdlc.model;

public record AffectedFile(
        String path,
        String reason,
        String changeType
) {
}