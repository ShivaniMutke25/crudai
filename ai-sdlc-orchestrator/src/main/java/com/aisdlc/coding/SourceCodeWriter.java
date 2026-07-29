package com.aisdlc.coding;

import com.aisdlc.coding.model.CodeChange;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SourceCodeWriter {

    public void write(CodeChange change){

        try{

            Files.writeString(

                    Path.of(change.filePath()),

                    change.updatedContent()

            );

        }

        catch (Exception ex){

            throw new RuntimeException(ex);

        }

    }

}