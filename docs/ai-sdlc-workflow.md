# AI-SDLC Workflow

The AI-SDLC orchestrator follows a simple story-driven workflow:

1. Load a story JSON file from the stories directory.
2. Parse the acceptance criteria and implementation steps.
3. Generate a phased plan with analysis, implementation, and validation steps.
4. Return the plan so it can be consumed by further automation or human review.

## Current Phase 1 Scope

This initial implementation focuses on the structure and contract of the orchestrator:

- separate Node.js module for orchestration
- story file input format
- workflow planning output
- test coverage for loading and planning
Yes. At this point, your project has a clean separation between **understanding the story**, **understanding the repository**, and **planning the implementation**.

## Overall Phase 4 → 5 → 6

```text
                         EMP-101.json
                              │
                              ▼
                    ┌──────────────────┐
                    │ WorkflowController│
                    └────────┬─────────┘
                             │
                             ▼
                  ┌──────────────────────┐
                  │ AiSdlcOrchestrator   │
                  │ Controls SDLC flow   │
                  └──────────┬───────────┘
                             │
            ┌────────────────┼─────────────────┐
            │                │                 │
            ▼                ▼                 ▼
         PHASE 4          PHASE 5           PHASE 6
      Story Analysis   Repo Analysis     Implementation
                                             Plan
```

# Phase 4 — Understand the Jira Story

### Goal

Phase 4 answers:

> **"What exactly is this Jira story asking us to build?"**

It does NOT inspect code yet.

```text
POST /api/workflow/analyze/EMP-101
                  │
                  ▼
        WorkflowController
                  │
                  ▼
        AiSdlcOrchestrator
                  │
                  │ analyzeStory("EMP-101")
                  ▼
        StoryReaderService
                  │
                  │ readStory()
                  ▼
           EMP-101.json
                  │
                  ▼
              JiraStory
                  │
                  ▼
       StoryAnalysisService
                  │
                  ▼
        Spring AI ChatClient
                  │
                  ▼
              Ollama
                  │
                  ▼
           Local LLM
                  │
                  ▼
          StoryAnalysis
```

### Classes involved

```text
EMP-101.json
     │
     ▼
StoryReaderService
     │
     │ converts JSON → Java object
     ▼
JiraStory
     │
     ▼
StoryAnalysisService
     │
     │ sends prompt
     ▼
ChatClient
     │
     ▼
Ollama
     │
     ▼
StoryAnalysis
```

`JiraStory` represents the raw input:

```text
storyId
title
description
acceptanceCriteria
priority
```

The AI converts that into structured engineering understanding:

```text
StoryAnalysis

summary
backendChangesRequired
frontendChangesRequired
databaseChangesRequired
risk
requirements
```

So Phase 4 is essentially:

```text
BUSINESS REQUIREMENT
        ↓
       AI
        ↓
TECHNICAL REQUIREMENT UNDERSTANDING
```

---

# Phase 5 — Understand the Actual Repository

Now we know **what needs to change**, but we don't yet know:

> **"Where in this actual codebase should it change?"**

That's Phase 5.

```text
                JiraStory
                    +
              StoryAnalysis
                    │
                    ▼
        RepositoryAnalysisService
                    │
          ┌─────────┴──────────┐
          │                    │
          ▼                    │
RepositoryContextService       │
          │                    │
          ▼                    │
     Scan actual repo          │
          │                    │
    ┌─────┴──────┐             │
    ▼            ▼             │
Spring Boot     React          │
src             src            │
    │            │             │
    └─────┬──────┘             │
          ▼                    │
   Rank relevant files         │
          │                    │
          ▼                    │
   Repository Context ─────────┘
          │
          ▼
 RepositoryAnalysisService
          │
          ▼
     Spring AI
          │
          ▼
       Ollama
          │
          ▼
 AI RepositoryAnalysis
          │
          ▼
RepositoryAnalysisValidator
          │
    ┌─────┴──────────┐
    │                │
Real paths        Fake paths
    │                │
   KEEP             REJECT
    │
    └───────┬────────┘
            ▼
 Verified RepositoryAnalysis
```

There are **three important classes** here.

### `RepositoryContextService`

This is **not AI**.

It's deterministic Java.

```text
JiraStory
   │
   ▼
Extract keywords

employee
department
update
create
list
   │
   ▼
Search:

springboot-backend/src
react-frontend/src
   │
   ▼
Score relevant files
   │
   ▼
Select top files
   │
   ▼
Read contents
   │
   ▼
Compact Repository Context
```

Why?

Because asking an LLM to crawl the entire filesystem is slow and unreliable.

Java is better at:

```text
finding files
reading files
checking paths
filtering files
```

The LLM is better at:

```text
understanding code
connecting requirements to code
reasoning about impact
```

That's an important design decision.

---

### `RepositoryAnalysisService`

Now we give Ollama:

```text
Jira Story
      +
Story Analysis
      +
Actual relevant source code
      ↓
    Ollama
      ↓
Repository impact
```

It answers:

> "Based on the actual code I received, these files are affected."

For example:

```text
Employee.java
    ↓
Employee model needs department

CreateEmployeeComponent.js
    ↓
Form needs department field

ListEmployeeComponent.js
    ↓
List needs to display department
```

---

### `RepositoryAnalysisValidator`

This is a **guardrail**.

We discovered why we need it when Ollama produced something like:

```text
DepartmentService.java
```

even though that file might not exist.

So we don't blindly trust the LLM.

```text
LLM says:

"Modify DepartmentService.java"
          │
          ▼
RepositoryAnalysisValidator
          │
          ▼
Files.exists(path)?
       /       \
     YES        NO
      │          │
    ACCEPT     REJECT
```

It also fixes invalid output such as:

```text
AI:
changeType = "Backend"

         ↓ validator

changeType = "MODIFY"
```

So Phase 5 ends with:

```text
Verified RepositoryAnalysis

storyId
affectedFiles
backendFindings
frontendFindings
risks
```

That's much safer.

---

# Phase 6 — Decide HOW to Implement It

Now we know:

```text
Phase 4
WHAT does the story require?

Phase 5
WHERE does the repository need changes?

Phase 6
HOW should we implement those changes?
```

Flow:

```text
               JiraStory
                   +
              StoryAnalysis
                   +
         Verified RepositoryAnalysis
                   +
         Actual Repository Context
                   │
                   ▼
       ImplementationPlanningService
                   │
                   ▼
            Spring AI ChatClient
                   │
                   ▼
                 Ollama
                   │
                   ▼
          ImplementationPlan
                   │
                   ▼
          ApprovalService
                   │
                   ▼
                PENDING
                   │
             ┌─────┴─────┐
             ▼           ▼
          APPROVE      REJECT
             │
             ▼
       Phase 7 allowed
```

### `ImplementationPlanningService`

It receives information from the previous stages:

```text
EMP-101

"Add department"
       +
StoryAnalysis

"Backend + frontend + DB affected"
       +
RepositoryAnalysis

"Employee.java affected"
"CreateEmployeeComponent.js affected"
       +
RepositoryContext

Actual source code
       ↓
ImplementationPlanningService
       ↓
Ollama
       ↓
ImplementationPlan
```

The result might be:

```text
STEP 1
MODIFY Employee.java
Add department field

STEP 2
MODIFY CreateEmployeeComponent.js
Add department input

STEP 3
MODIFY UpdateEmployeeComponent.js
Allow department editing

STEP 4
MODIFY ListEmployeeComponent.js
Display department

STEP 5
TEST
Verify create/update/list functionality
```

Still **no code modification**.

---

# Human approval

This is one of the most important parts of your architecture.

After Phase 6:

```text
AI Implementation Plan
          │
          ▼
   ApprovalService
          │
          ▼
       PENDING
```

A developer/tech lead reviews it.

```text
                 PENDING
                    │
          ┌─────────┴─────────┐
          │                   │
          ▼                   ▼
       APPROVED            REJECTED
          │
          ▼
Phase 7 Coding Agent
can start
```

Why?

Because we don't want:

```text
Jira
 ↓
AI
 ↓
AI edits everything
 ↓
AI pushes code
 ↓
💥
```

Instead:

```text
Jira
 ↓
AI understands
 ↓
AI investigates
 ↓
AI proposes plan
 ↓
👤 HUMAN APPROVAL
 ↓
AI gets write permission
```

That's a much more production-grade AI SDLC design.

---

# Complete class-to-class diagram

This is the diagram I'd use when explaining your project in the evaluation:

```text
                     ┌─────────────────────┐
                     │    EMP-101.json     │
                     │ Simulated Jira Story│
                     └──────────┬──────────┘
                                │
                                ▼
                     ┌─────────────────────┐
                     │ StoryReaderService  │
                     └──────────┬──────────┘
                                │
                                ▼
                          ┌───────────┐
                          │ JiraStory │
                          └─────┬─────┘
                                │
════════════════════════════════╪══════════════════════════
              PHASE 4 — STORY UNDERSTANDING
                                │
                                ▼
                  ┌────────────────────────┐
                  │ StoryAnalysisService   │
                  └────────────┬───────────┘
                               │
                         Spring AI
                               │
                               ▼
                            Ollama
                               │
                               ▼
                    ┌───────────────────┐
                    │  StoryAnalysis    │
                    │                   │
                    │ requirements      │
                    │ backend?          │
                    │ frontend?         │
                    │ database?         │
                    │ risk              │
                    └─────────┬─────────┘
                              │
══════════════════════════════╪════════════════════════════
            PHASE 5 — REPOSITORY UNDERSTANDING
                              │
                              ▼
                 ┌─────────────────────────┐
                 │RepositoryContextService │
                 └────────────┬────────────┘
                              │
                   ┌──────────┴──────────┐
                   ▼                     ▼
            Spring Boot src          React src
                   │                     │
                   └──────────┬──────────┘
                              │
                              ▼
                     Relevant Source Code
                              │
                              ▼
                 ┌────────────────────────┐
                 │RepositoryAnalysisService│
                 └────────────┬───────────┘
                              │
                           Ollama
                              │
                              ▼
                    AI RepositoryAnalysis
                              │
                              ▼
              ┌─────────────────────────────┐
              │RepositoryAnalysisValidator  │
              │                             │
              │ • verify paths              │
              │ • reject hallucinations     │
              │ • normalize output          │
              └──────────────┬──────────────┘
                             │
                             ▼
                 Verified RepositoryAnalysis
                             │
═════════════════════════════╪═════════════════════════════
              PHASE 6 — IMPLEMENTATION PLANNING
                             │
                             ▼
              ┌──────────────────────────────┐
              │ImplementationPlanningService │
              └──────────────┬───────────────┘
                             │
                          Ollama
                             │
                             ▼
                  ┌────────────────────┐
                  │ImplementationPlan  │
                  │                    │
                  │ Step 1             │
                  │ Step 2             │
                  │ Step 3             │
                  │ Tests              │
                  │ Risks              │
                  └─────────┬──────────┘
                            │
                            ▼
                    ┌───────────────┐
                    │ApprovalService│
                    └───────┬───────┘
                            │
                         PENDING
                            │
                     ┌──────┴──────┐
                     ▼             ▼
                 APPROVED       REJECTED
                     │
                     ▼
              PHASE 7 — CODING
```

## The easiest way to remember it

For your interview, remember just **three questions**:

```text
PHASE 4
"What does the story require?"
        ↓
StoryAnalysis


PHASE 5
"Where in the real codebase is the impact?"
        ↓
Verified RepositoryAnalysis


PHASE 6
"How should we implement it safely?"
        ↓
ImplementationPlan
        ↓
Human Approval
```

And then Phase 7 will answer:

```text
"Now actually make the approved code changes."
```

That separation—**Understand → Investigate → Plan → Approve → Execute**—is the core of your AI-native SDLC workflow.
