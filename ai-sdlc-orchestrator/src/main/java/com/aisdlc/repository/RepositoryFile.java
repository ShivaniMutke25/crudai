package com.aisdlc.repository.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepositoryFile {

    private String path;

    private String content;

}