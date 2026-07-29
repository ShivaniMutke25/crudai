package com.aisdlc.agent;

import com.aisdlc.model.TestResult;
import com.aisdlc.model.WorkflowContext;
import com.aisdlc.testing.FrontendBuildService;
import com.aisdlc.testing.MavenBuildService;
import com.aisdlc.testing.TestReportParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestingAgent implements Agent {

    private final MavenBuildService backend;

    private final FrontendBuildService frontend;

    private final TestReportParser parser;

    @Override
    public void execute(WorkflowContext context){

        log.info("Testing Agent Started");

        TestResult backendResult =
                backend.execute("springboot-backend");

        TestResult frontendResult =
                frontend.execute("react-frontend");

        TestResult finalResult =
                TestResult.builder()

                        .passed(
                                backendResult.isPassed()

                                        &&

                                frontendResult.isPassed()
                        )

                        .report(

                                parser.summarize(

                                        backendResult.getReport(),

                                        frontendResult.getReport()

                                )

                        )

                        .build();

        context.setTestResult(finalResult);

        log.info("Testing Agent Finished");

    }

}