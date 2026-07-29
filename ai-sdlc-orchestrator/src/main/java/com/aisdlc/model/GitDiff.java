package com.aisdlc.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GitDiff {

    private String diff;

    private boolean success;

}