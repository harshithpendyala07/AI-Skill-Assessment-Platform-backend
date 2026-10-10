package com.aiskill.platform.dto;

public class CodeExecutionRequest {
    private Long problemId;
    private String code;
    private String language; // "java", "python", "javascript"

    public CodeExecutionRequest() {}

    public Long getProblemId() { return problemId; }
    public void setProblemId(Long problemId) { this.problemId = problemId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
