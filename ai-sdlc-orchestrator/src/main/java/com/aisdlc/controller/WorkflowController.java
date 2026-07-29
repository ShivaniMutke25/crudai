package com.aisdlc.controller;

import com.aisdlc.model.JiraStory;
import com.aisdlc.model.WorkflowContext;
import com.aisdlc.orchestrator.AgentOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workflow")
@RequiredArgsConstructor
public class WorkflowController {

    private final AgentOrchestrator agentOrchestrator;

    @PostMapping("/start")
    public ResponseEntity<WorkflowContext> startWorkflow(
            @RequestBody JiraStory jiraStory) {

        try {

            WorkflowContext context =
                    agentOrchestrator.execute(jiraStory);

            return ResponseEntity.ok(context);

        } catch (Exception ex) {

            ex.printStackTrace();

            return ResponseEntity.internalServerError().build();

        }
    }

}