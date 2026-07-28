package com.aisdlc.service;

import com.aisdlc.model.ApprovalStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ApprovalService {

    private final Map<String, ApprovalStatus> approvals =
            new ConcurrentHashMap<>();

    public ApprovalStatus createPending(String storyId) {

        approvals.put(
                storyId,
                ApprovalStatus.PENDING
        );

        return ApprovalStatus.PENDING;
    }

    public ApprovalStatus approve(String storyId) {

        approvals.put(
                storyId,
                ApprovalStatus.APPROVED
        );

        return ApprovalStatus.APPROVED;
    }

    public ApprovalStatus reject(String storyId) {

        approvals.put(
                storyId,
                ApprovalStatus.REJECTED
        );

        return ApprovalStatus.REJECTED;
    }

    public ApprovalStatus getStatus(String storyId) {

        return approvals.getOrDefault(
                storyId,
                ApprovalStatus.PENDING
        );
    }
}