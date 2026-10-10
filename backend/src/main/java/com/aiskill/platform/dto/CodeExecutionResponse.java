package com.aiskill.platform.dto;

import java.util.List;

public class CodeExecutionResponse {
    private String status; // "Accepted", "Wrong Answer", "Compile Error", "Runtime Error"
    private Integer passedTestCases;
    private Integer totalTestCases;
    private Integer score;
    private Long executionTimeMs;
    private String output;
    private List<TestCaseResult> testCaseResults;

    public static class TestCaseResult {
        private String input;
        private String expectedOutput;
        private String actualOutput;
        private boolean passed;

        public TestCaseResult() {}

        public TestCaseResult(String input, String expectedOutput, String actualOutput, boolean passed) {
            this.input = input;
            this.expectedOutput = expectedOutput;
            this.actualOutput = actualOutput;
            this.passed = passed;
        }

        public String getInput() { return input; }
        public void setInput(String input) { this.input = input; }

        public String getExpectedOutput() { return expectedOutput; }
        public void setExpectedOutput(String expectedOutput) { this.expectedOutput = expectedOutput; }

        public String getActualOutput() { return actualOutput; }
        public void setActualOutput(String actualOutput) { this.actualOutput = actualOutput; }

        public boolean isPassed() { return passed; }
        public void setPassed(boolean passed) { this.passed = passed; }
    }

    public CodeExecutionResponse() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getPassedTestCases() { return passedTestCases; }
    public void setPassedTestCases(Integer passedTestCases) { this.passedTestCases = passedTestCases; }

    public Integer getTotalTestCases() { return totalTestCases; }
    public void setTotalTestCases(Integer totalTestCases) { this.totalTestCases = totalTestCases; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }

    public List<TestCaseResult> getTestCaseResults() { return testCaseResults; }
    public void setTestCaseResults(List<TestCaseResult> testCaseResults) { this.testCaseResults = testCaseResults; }
}
