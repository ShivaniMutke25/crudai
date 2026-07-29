package com.aisdlc.repository;

import java.util.List;

public record RepositoryContext(

        List<RepositoryFile> files

) {}