package com.aisdlc.repository;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepositoryFile {

    private String path;

    private String content;

}