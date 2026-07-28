package com.aisdlc.entity;

import com.aisdlc.model.ApprovalStatus;
import com.aisdlc.model.WorkflowPhase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "workflow_state")
public class WorkflowStateEntity {

    @Id
    private String storyId;

    @Enumerated(EnumType.STRING)
    private WorkflowPhase currentPhase;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String storyAnalysisJson;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String repositoryAnalysisJson;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String implementationPlanJson;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public WorkflowStateEntity() {
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public String getStoryId() {
    return storyId;
}

public void setStoryId(String storyId) {
    this.storyId = storyId;
}

public WorkflowPhase getCurrentPhase() {
    return currentPhase;
}

public void setCurrentPhase(WorkflowPhase currentPhase) {
    this.currentPhase = currentPhase;
}

public ApprovalStatus getApprovalStatus() {
    return approvalStatus;
}

public void setApprovalStatus(ApprovalStatus approvalStatus) {
    this.approvalStatus = approvalStatus;
}

public String getStoryAnalysisJson() {
    return storyAnalysisJson;
}

public void setStoryAnalysisJson(String storyAnalysisJson) {
    this.storyAnalysisJson = storyAnalysisJson;
}

public String getRepositoryAnalysisJson() {
    return repositoryAnalysisJson;
}

public void setRepositoryAnalysisJson(String repositoryAnalysisJson) {
    this.repositoryAnalysisJson = repositoryAnalysisJson;
}

public String getImplementationPlanJson() {
    return implementationPlanJson;
}

public void setImplementationPlanJson(String implementationPlanJson) {
    this.implementationPlanJson = implementationPlanJson;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}

public LocalDateTime getUpdatedAt() {
    return updatedAt;
}

public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
}
}