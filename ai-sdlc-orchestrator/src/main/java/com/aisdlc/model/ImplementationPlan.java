package com.aisdlc.model;

import java.util.List;

public record ImplementationPlan(

        String storyId,
        String summary,
        List<ImplementationStep> steps,
        List<String> searchKeywords,
        List<String> testStrategy,
        List<String> risks,
        boolean requiresHumanApproval

) {
}