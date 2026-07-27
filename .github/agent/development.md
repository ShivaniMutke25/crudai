# AI-Native SDLC Workflow Plan

This document captures the phased plan for building an AI-native SDLC workflow around the CRUD employee application.

## Goal

Create an incremental proof-of-concept that demonstrates a story-driven AI workflow spanning:

- story intake
- repository understanding
- implementation planning
- code changes
- testing and self-repair
- review and pull request generation

The final POC will use a separate orchestrator service so the AI workflow remains independent from the employee application itself.

## Target Architecture

```text
Jira Story
    │
    ▼
Story Reader
    │
    ▼
Story Analyst ← LLM
    │
    ▼
Repo Context Analyzer
    │
    ▼
Planning Agent ← LLM
    │
    ▼
Human Approval
    │
    ▼
Coding Agent ← LLM + Tools
    │
    ▼
Code Changes
    │
    ▼
Testing Agent
    │
    ┌──────┬──────┐
    ▼      ▼
FAIL   PASS
    │      │
    ▼      ▼
AI Repair  Review Agent
    │
    ▼
Security / Quality
    │
    ▼
Git Diff
    │
    ▼
PR Agent
    │
    ▼
GitHub PR
    │
    ▼
Human Review
```

## Recommended Stack

- Spring Boot for the orchestrator service
- Spring AI for LLM integration
- Java as the primary implementation language for the orchestration layer
- The existing Spring Boot backend and React frontend remain separate application components

## Phase 1 — Create the AI-SDLC Service

Build a separate service at the repository root:

```text
crud-employee/
├── springboot-backend/
├── react-frontend/
├── ai-sdlc-orchestrator/
├── stories/
├── docs/
└── README.md
```

This separation keeps AI orchestration logic independent from the employee application and makes the design reusable for other repositories.

## Phase 2 — First Jira Story

For the first iteration, simulate Jira with a local JSON story instead of integrating Jira immediately.

Example story:

```json
{
  "key": "EMP-101",
  "title": "Add department to employee",
  "description": "As an HR administrator, I want to assign a department to an employee so that employees can be organized by department.",
  "acceptanceCriteria": [
    "Department is mandatory when creating an employee",
    "Department can be updated",
    "Department must be displayed in the employee list",
    "Existing employee functionality must continue working"
  ],
  "priority": "Medium"
}
```

## Phase 3 — Build the Spring AI Orchestrator

The orchestrator will progressively include the following components:

```text
ai-sdlc-orchestrator/
└── src/main/java/
    ├── controller/
    │   └── WorkflowController.java
    ├── agent/
    │   ├── StoryAnalysisAgent.java
    │   ├── PlanningAgent.java
    │   ├── CodingAgent.java
    │   ├── TestingAgent.java
    │   └── ReviewAgent.java
    ├── tool/
    │   ├── RepositoryTool.java
    │   ├── FileTool.java
    │   ├── MavenTool.java
    │   └── GitTool.java
    ├── model/
    │   ├── JiraStory.java
    │   ├── StoryAnalysis.java
    │   ├── ImplementationPlan.java
    │   └── ReviewResult.java
    └── service/
        └── AiSdlcOrchestrator.java
```

At first, only a subset of these classes will be introduced.

## Phase 4 — First Working AI Call

The first milestone should be small and measurable:

```text
EMP-101.json
    ↓
Spring Boot
    ↓
Spring AI
    ↓
OpenAI
    ↓
Structured Story Analysis
```

Example endpoint:

```text
POST /api/workflow/analyze/EMP-101
```

Expected response shape:

```json
{
  "storyId": "EMP-101",
  "summary": "Add department support to employee management",
  "backendChangesRequired": true,
  "frontendChangesRequired": true,
  "databaseChangesRequired": true,
  "risk": "LOW",
  "requirements": [
    "Add department to employee",
    "Validate department",
    "Display department",
    "Allow department updates"
  ]
}
```

## Phase 5 — Repository Context

The AI should use tools such as:

- readFile(path)
- listFiles(directory)
- searchCode(keyword)
- getProjectStructure()

This allows the orchestrator to inspect the repository instead of hallucinating file locations.

## Phase 6 — Planning to Coding

The workflow should move from story to implementation plan to actual code changes:

```text
Story
    ↓
Repository Context
    ↓
Planning Agent
    ↓
Implementation Plan
    ↓
Human Approval
    ↓
Coding Agent
    ↓
Actual file changes
```

The plan should cover:

- backend work
- frontend work
- testing work

## Phase 7 — Tests and Self-Repair

The orchestrator should run validation commands such as:

- mvn test
- npm test

If tests fail, the workflow should attempt a limited repair loop before stopping.

## Phase 8 — Git and PR

The final workflow should support:

```text
git checkout -b ai/EMP-101-add-department
    ↓
git diff
    ↓
Review Agent
    ↓
Tests PASS
    ↓
git commit
    ↓
push branch
    ↓
GitHub PR
```

## What Not to Build Yet

Avoid starting with:

- Jira MCP
- GitHub MCP
- pgvector
- advanced RAG
- Kafka
- Kubernetes
- AWS
- many separate agents

The first goal is to prove the core flow:

```text
Story
    ↓
AI understands
    ↓
AI understands repository
    ↓
AI plans
    ↓
AI changes code
    ↓
Tests
    ↓
AI reviews
    ↓
PR
```

## Success Criteria

The initial POC is successful when the system can:

1. read a story file
2. analyze the story with an LLM
3. inspect repository structure and relevant files
4. generate an implementation plan
5. modify code
6. run tests
7. produce a reviewable Git diff or PR draft
