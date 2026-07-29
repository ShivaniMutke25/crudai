package com.aisdlc.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestResult {

    private boolean passed;

    private String report;

}