package com.aisdlc.agent;

import com.aisdlc.model.WorkflowContext;

public interface Agent {

    void execute(WorkflowContext context) throws Exception;

}