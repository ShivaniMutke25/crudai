package com.aisdlc.repository;

import com.aisdlc.repository.*;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepositoryContextBuilder {

    public RepositoryContext build(
            List<RepositoryMetadata> metadata) {

        List<RepositoryFile> files = new ArrayList<>();

        for (RepositoryMetadata file : metadata) {

            try {

                files.add(

                        RepositoryFile.builder()

                                .path(file.getPath())

                                .content(
                                        Files.readString(
                                                Path.of(file.getPath())
                                        ))

                                .build()

                );

            } catch (IOException e) {

                throw new RuntimeException(e);

            }

        }

        return RepositoryContext.builder()

                .files(files)

                .build();

    }

}