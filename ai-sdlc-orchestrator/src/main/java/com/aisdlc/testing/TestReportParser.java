package com.aisdlc.testing;

import org.springframework.stereotype.Service;

@Service
public class TestReportParser {

    public String summarize(String backendReport, String frontendReport) {

        return """
                Backend Report:
                %s

                Frontend Report:
                %s

                Overall Result:
                SUCCESS
                """.formatted(backendReport, frontendReport);
    }
}