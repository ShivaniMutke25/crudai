package com.aisdlc.repository;

import com.aisdlc.entity.WorkflowStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowStateRepository
        extends JpaRepository<WorkflowStateEntity, String> {
}