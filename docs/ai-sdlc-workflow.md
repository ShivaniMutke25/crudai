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
