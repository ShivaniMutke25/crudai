package com.aisdlc.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PullRequestResult {

    private String title;

    private String description;

    private String url;

    private String status;

}