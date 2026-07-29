package com.aisdlc.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PullRequestResult {

    private String url;

    private String status;

}