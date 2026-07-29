package com.aisdlc.model;

import java.util.List;

public record RepositoryAnalysis(

        String summary,

        List<String> relevantFiles

) {}