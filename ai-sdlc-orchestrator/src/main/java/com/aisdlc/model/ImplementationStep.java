package com.aisdlc.model;

public record ImplementationStep(
        int order,
        String filePath,
        String action,
        String description,
        String layer
) {
}