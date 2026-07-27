# Architecture Overview

This repository now contains a separate AI-SDLC orchestrator module that is intentionally decoupled from the existing employee backend and frontend.

## Components

- springboot-backend: the existing CRUD employee API.
- react-frontend: the existing UI for managing employees.
- ai-sdlc-orchestrator: a standalone Node.js service for loading stories, planning workflow phases, and preparing implementation tasks.
- stories: example story definitions that drive the orchestrator.
- docs: architecture and workflow documentation.

## Design Goals

- Keep AI orchestration logic separate from the employee application business logic.
- Allow the same orchestrator to be reused for other repositories in the future.
- Use a story-driven workflow so implementation tasks can be planned and validated consistently.
