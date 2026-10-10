package com.aiskill.platform.dto;

import java.util.List;

public class ResumeAnalyzeRequest {
    private String resumeText;
    private String targetRole;

    public ResumeAnalyzeRequest() {}

    public String getResumeText() { return resumeText; }
    public void setResumeText(String resumeText) { this.resumeText = resumeText; }

    public String getTargetRole() { return targetRole; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
}
