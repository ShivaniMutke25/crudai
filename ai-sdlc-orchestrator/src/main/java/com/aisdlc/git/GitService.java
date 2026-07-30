package com.aisdlc.github;

import com.aisdlc.model.PullRequestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GitHubService {

    public PullRequestResult createPullRequest(String title,
                                               String description) {

        log.info("======================================");
        log.info("Creating Pull Request");
        log.info("Title       : {}", title);
        log.info("Description : {}", description);
        log.info("======================================");

        return PullRequestResult.builder()
                .status("SUCCESS")
                .url("https://github.com/demo/demo/pull/1")
                .build();
    }

}