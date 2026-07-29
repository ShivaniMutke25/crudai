package com.aisdlc.coding;

import com.aisdlc.repository.model.RepositoryFile;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SourceCodeReader {

    public RepositoryFile read(String filePath) {

        try {

            return new RepositoryFile(

                    filePath,

                    Files.readString(Path.of(filePath))

            );

        }

        catch (Exception ex){

            throw new RuntimeException(ex);

        }

    }

}