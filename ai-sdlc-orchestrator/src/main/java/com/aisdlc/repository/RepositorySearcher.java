package com.aisdlc.repository;

import com.aisdlc.repository.RepositoryMetadata;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepositorySearcher {

    public List<RepositoryMetadata> search(
            List<RepositoryMetadata> repository,
            List<String> keywords) {

        return repository.stream()

                .filter(file ->
                        keywords.stream().anyMatch(k ->
                                file.getFileName().toLowerCase()
                                        .contains(k.toLowerCase())))

                .collect(Collectors.toList());

    }

}