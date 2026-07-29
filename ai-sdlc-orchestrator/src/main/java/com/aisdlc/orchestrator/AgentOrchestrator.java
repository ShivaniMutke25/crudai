package com.aisdlc.orchestrator;

import com.aisdlc.agent.*;
import com.aisdlc.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentOrchestrator {

    private final PlannerAgent plannerAgent;
    private final RepositoryAgent repositoryAgent;
    private final CodingAgent codingAgent;
    private final TestingAgent testingAgent;
    private final ReviewAgent reviewAgent;
    private final PullRequestAgent pullRequestAgent;

    public WorkflowContext execute(JiraStory story) throws Exception {

        WorkflowContext context = WorkflowContext.builder()
                .jiraStory(story)
                .build();

        plannerAgent.execute(context);

        repositoryAgent.execute(context);

        codingAgent.execute(context);

        testingAgent.execute(context);

        reviewAgent.execute(context);

        if (context.getReviewResult().isApproved()) {

            pullRequestAgent.execute(context);

        }

        return context;

    }

}