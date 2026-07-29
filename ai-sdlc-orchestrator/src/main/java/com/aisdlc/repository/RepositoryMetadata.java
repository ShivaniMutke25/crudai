package com.aisdlc.repository.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepositoryMetadata {

    private String path;

    private String fileName;

    private String type;

}