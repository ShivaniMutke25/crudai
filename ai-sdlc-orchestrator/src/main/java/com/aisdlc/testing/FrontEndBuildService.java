// package com.aisdlc.testing;

// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Service;

// import java.io.BufferedReader;
// import java.io.InputStreamReader;

// @Slf4j
// @Service
// public class MavenBuildService {
// // 
//     public TestResult execute(String projectPath) {

//         try {

//             ProcessBuilder builder = new ProcessBuilder(
//                     "mvn",
//                     "clean",
//                     "test"
//             );

//             builder.directory(new java.io.File(projectPath));

//             Process process = builder.start();

//             StringBuilder output = new StringBuilder();

//             try (BufferedReader reader =
//                          new BufferedReader(
//                                  new InputStreamReader(
//                                          process.getInputStream()))) {

//                 String line;

//                 while ((line = reader.readLine()) != null) {

//                     output.append(line).append("\n");

//                 }

//             }

//             int exitCode = process.waitFor();

//             return TestResult.builder()
//                     .passed(exitCode == 0)
//                     .report(output.toString())
//                     .build();

//         }

//         catch (Exception ex) {

//             throw new RuntimeException(ex);

//         }

//     }

// }