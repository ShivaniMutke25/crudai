package com.aisdlc.repository;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class RepositoryWriter {

    public void write(String path,
                      String content) {

        try {

            Files.writeString(

                    Path.of(path),

                    content

            );

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }

}