package com.aisdlc.service;

import com.aisdlc.entity.WorkflowStateEntity;
import com.aisdlc.model.ApprovalStatus;
import com.aisdlc.model.ImplementationPlan;
import com.aisdlc.model.RepositoryAnalysis;
import com.aisdlc.model.StoryAnalysis;
import com.aisdlc.model.WorkflowPhase;
import com.aisdlc.repository.WorkflowStateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class WorkflowStateService {

    private final WorkflowStateRepository repository;
    private final ObjectMapper objectMapper;

    public WorkflowStateService(
            WorkflowStateRepository repository,
            ObjectMapper objectMapper) {

        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public void saveStoryAnalysis(
            String storyId,
            StoryAnalysis storyAnalysis) {

        WorkflowStateEntity entity = repository.findById(storyId)
                .orElseGet(WorkflowStateEntity::new);

        entity.setStoryId(storyId);
        entity.setCurrentPhase(WorkflowPhase.STORY_ANALYZED);

        try {
            entity.setStoryAnalysisJson(
                    objectMapper.writeValueAsString(storyAnalysis)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        repository.save(entity);
    }

    public StoryAnalysis loadStoryAnalysis(String storyId) {

        return repository.findById(storyId)
                .map(WorkflowStateEntity::getStoryAnalysisJson)
                .filter(json -> json != null)
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, StoryAnalysis.class);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElse(null);
    }

    public void saveRepositoryAnalysis(
            String storyId,
            RepositoryAnalysis repositoryAnalysis) {

        WorkflowStateEntity entity = repository.findById(storyId)
                .orElseGet(WorkflowStateEntity::new);

        entity.setStoryId(storyId);
        entity.setCurrentPhase(WorkflowPhase.REPOSITORY_ANALYZED);

        try {
            entity.setRepositoryAnalysisJson(
                    objectMapper.writeValueAsString(repositoryAnalysis)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        repository.save(entity);
    }

    public RepositoryAnalysis loadRepositoryAnalysis(String storyId) {

        return repository.findById(storyId)
                .map(WorkflowStateEntity::getRepositoryAnalysisJson)
                .filter(json -> json != null)
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, RepositoryAnalysis.class);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElse(null);
    }

    public void saveImplementationPlan(
            String storyId,
            ImplementationPlan implementationPlan,
            ApprovalStatus approvalStatus) {

        WorkflowStateEntity entity = repository.findById(storyId)
                .orElseGet(WorkflowStateEntity::new);

        entity.setStoryId(storyId);
        entity.setCurrentPhase(WorkflowPhase.PLAN_CREATED);
        entity.setApprovalStatus(approvalStatus);

        try {
            entity.setImplementationPlanJson(
                    objectMapper.writeValueAsString(implementationPlan)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        repository.save(entity);
    }

    public ImplementationPlan loadImplementationPlan(String storyId) {

        return repository.findById(storyId)
                .map(WorkflowStateEntity::getImplementationPlanJson)
                .filter(json -> json != null)
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, ImplementationPlan.class);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElse(null);
    }
}