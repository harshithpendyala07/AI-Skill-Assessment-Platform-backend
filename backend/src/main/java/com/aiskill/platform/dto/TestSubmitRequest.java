package com.aiskill.platform.dto;

import java.util.Map;

public class TestSubmitRequest {
    private Map<Long, String> answers; // questionId -> selectedOption ("A", "B", "C", "D")
    private Integer timeSpentSeconds;

    public TestSubmitRequest() {}

    public Map<Long, String> getAnswers() { return answers; }
    public void setAnswers(Map<Long, String> answers) { this.answers = answers; }

    public Integer getTimeSpentSeconds() { return timeSpentSeconds; }
    public void setTimeSpentSeconds(Integer timeSpentSeconds) { this.timeSpentSeconds = timeSpentSeconds; }
}
