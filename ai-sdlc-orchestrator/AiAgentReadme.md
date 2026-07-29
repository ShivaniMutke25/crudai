# AI SDLC Orchestrator

An AI-powered Software Development Lifecycle (SDLC) orchestration platform that automates the journey from a Jira story to a GitHub Pull Request using multiple AI agents.

The project demonstrates how an AI Native Engineer would design a production-ready development workflow by combining Large Language Models (LLMs), Spring Boot, GitHub, and CI/CD practices.

---

# Architecture

```
                +------------------+
                |    Jira Story    |
                +--------+---------+
                         |
                         v
               +-------------------+
               | Agent Orchestrator|
               +--------+----------+
                        |
        -----------------------------------------
        |        |        |        |            |
        v        v        v        v            v
 Planner  Repository  Coding  Testing   Review
  Agent      Agent     Agent    Agent     Agent
        \__________________________________/
                        |
                        v
                Pull Request Agent
                        |
                        v
                  GitHub Pull Request
```

---

# AI Agent Workflow

## 1. Planner Agent

### Input
- Jira Story

### Responsibilities

- Understand the Jira story
- Analyze acceptance criteria
- Break work into implementation steps
- Identify repository search keywords
- Create implementation strategy
- Decide whether human approval is required

### Output

```
ImplementationPlan
```

---

## 2. Repository Agent

### Input

```
ImplementationPlan
```

### Responsibilities

- Scan repository
- Search relevant files
- Read source code
- Build repository context
- Analyze impacted modules
- Identify affected services/classes/APIs

### Output

```
RepositoryAnalysis
```

---

## 3. Coding Agent

### Input

- Implementation Plan
- Repository Analysis
- Repository Context

### Responsibilities

- Generate production-ready code
- Update only impacted files
- Preserve coding standards
- Write generated code back to repository
- Generate Git Diff

### Output

```
GitDiff
```

---

## 4. Testing Agent

### Responsibilities

- Build backend project
- Run unit tests
- Run frontend tests
- Parse reports
- Determine build status

### Output

```
TestResult
```

---

## 5. Review Agent

### Responsibilities

AI acts as a senior reviewer.

It verifies

- Code quality
- SOLID principles
- Design patterns
- Test results
- Git Diff
- Requirement coverage

### Output

```
ReviewResult
```

---

## 6. Pull Request Agent

### Responsibilities

- Create Git branch
- Commit changes
- Push to GitHub
- Create Pull Request
- Return PR URL

### Output

```
PullRequestResult
```

---

# Overall Execution Flow

```
Jira Story
      │
      ▼
Planner Agent
      │
Implementation Plan
      ▼
Repository Agent
      │
Repository Analysis
      ▼
Coding Agent
      │
Git Diff
      ▼
Testing Agent
      │
Test Result
      ▼
Review Agent
      │
Review Result
      ▼
Pull Request Agent
      │
GitHub Pull Request
```

---

# Project Structure

```
src/main/java/com/aisdlc

├── agent
│     ├── PlannerAgent
│     ├── RepositoryAgent
│     ├── CodingAgent
│     ├── TestingAgent
│     ├── ReviewAgent
│     └── PullRequestAgent
│
├── orchestrator
│     └── AgentOrchestrator
│
├── controller
│     └── WorkflowController
│
├── repository
│     ├── RepositoryScanner
│     ├── RepositorySearcher
│     ├── RepositoryContextBuilder
│     └── RepositoryWriter
│
├── coding
│     ├── SourceCodeReader
│     ├── SourceCodeWriter
│     └── GitDiffGenerator
│
├── testing
│     ├── MavenBuildService
│     ├── FrontendBuildService
│     └── TestReportParser
│
├── github
│     └── GitHubClient
│
├── git
│     └── GitService
│
├── llm
│     └── LlmService
│
├── prompt
│     └── PromptLoader
│
└── model
      ├── WorkflowContext
      ├── JiraStory
      ├── ImplementationPlan
      ├── RepositoryAnalysis
      ├── GitDiff
      ├── TestResult
      ├── ReviewResult
      └── PullRequestResult
```

---

# Workflow Context

Every agent shares a common `WorkflowContext`.

```
WorkflowContext

├── JiraStory
├── ImplementationPlan
├── RepositoryAnalysis
├── GitDiff
├── TestResult
├── ReviewResult
└── PullRequestResult
```

Each agent enriches the context before passing it to the next agent.

---

# Technologies

- Java 21
- Spring Boot
- Spring AI
- OpenAI / Ollama
- Maven
- Git
- GitHub REST API
- JUnit
- React (Frontend Example)

---

# Agent Responsibilities

| Agent | Input | Output |
|--------|-------|--------|
| Planner | Jira Story | Implementation Plan |
| Repository | Implementation Plan | Repository Analysis |
| Coding | Repository Analysis | Git Diff |
| Testing | Generated Code | Test Result |
| Review | Git Diff + Tests | Review Result |
| Pull Request | Approved Review | Pull Request URL |

---

# Future Enhancements

- GitHub Webhook Integration
- Jira REST API Integration
- Azure DevOps Support
- Multi-Agent Parallel Execution
- Human Approval Dashboard
- Automatic Rollback
- Security & Vulnerability Scanning
- SonarQube Integration
- Docker Support
- Kubernetes Deployment
- Multi-LLM Support (OpenAI, Ollama, Claude, Gemini)

---

# End-to-End Flow

```
Jira Story
      │
      ▼
Planner Agent
      │
      ▼
Repository Agent
      │
      ▼
Coding Agent
      │
      ▼
Testing Agent
      │
      ▼
Review Agent
      │
      ▼
Pull Request Agent
      │
      ▼
GitHub Pull Request
```

---

## Goal

This project demonstrates a production-grade AI SDLC orchestration pipeline where autonomous AI agents collaborate to transform a Jira story into reviewed, tested, and production-ready code with minimal human intervention.