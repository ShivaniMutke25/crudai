package com.aisdlc.coding;

import com.aisdlc.model.GitDiff;
import org.springframework.stereotype.Service;

@Service
public class GitDiffGenerator {

    public GitDiff generate(){

        return GitDiff.builder()

                .success(true)

                .diff("Generated using git diff")

                .build();

    }

}