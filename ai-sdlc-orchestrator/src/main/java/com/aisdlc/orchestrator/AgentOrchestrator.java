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

        System.out.println("PlannerAgent Started");
plannerAgent.execute(context);

System.out.println("RepositoryAgent Started");
repositoryAgent.execute(context);

System.out.println("CodingAgent Started");
codingAgent.execute(context);

System.out.println("TestingAgent Started");
testingAgent.execute(context);

System.out.println("ReviewAgent Started");
reviewAgent.execute(context);

if (context.getReviewResult().isApproved()) {

    System.out.println("PullRequestAgent Started");
    pullRequestAgent.execute(context);

} else {

    System.out.println("Review rejected. Pull Request not created.");

}
        return context;

    }

}