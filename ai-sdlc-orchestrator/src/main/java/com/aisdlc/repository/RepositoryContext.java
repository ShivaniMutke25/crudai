package com.aisdlc.repository;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RepositoryContext {

    private List<RepositoryFile> files;

}