package com.aisdlc.controller;

import com.aisdlc.model.ApprovalStatus;
import com.aisdlc.service.AiSdlcOrchestrator;
import com.aisdlc.service.ApprovalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkflowController.class)
class WorkflowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AiSdlcOrchestrator orchestrator;

    @MockBean
    private ApprovalService approvalService;

    @Test
    void approveEndpointAcceptsGetRequests() throws Exception {
        when(approvalService.approve("story-1")).thenReturn(ApprovalStatus.APPROVED);

        mockMvc.perform(get("/api/workflow/approve/story-1"))
                .andExpect(status().isOk());

        verify(approvalService).approve("story-1");
    }

    @Test
    void approveEndpointAcceptsPostRequests() throws Exception {
        when(approvalService.approve("story-2")).thenReturn(ApprovalStatus.APPROVED);

        mockMvc.perform(post("/api/workflow/approve/story-2"))
                .andExpect(status().isOk());

        verify(approvalService).approve("story-2");
    }
}
