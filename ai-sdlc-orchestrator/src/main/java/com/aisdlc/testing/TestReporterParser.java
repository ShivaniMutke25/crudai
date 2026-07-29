package com.aisdlc.testing;

import org.springframework.stereotype.Service;

@Service
public class TestReportParser {

    public String summarize(String backend,
                            String frontend){

        return backend +

                "\n\n"

                + frontend;

    }

}