package com.aisdlc.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitDiff {

    private String diff;

}