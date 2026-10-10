package com.aiskill.platform.dto;

import java.util.List;

public class ResumeAnalyzeResponse {
    private String targetRole;
    private Integer matchPercentage;
    private List<String> extractedSkills;
    private List<String> missingSkills;
    private List<String> recommendedTopics;
    private String detailedSummary;

    public ResumeAnalyzeResponse() {}

    public ResumeAnalyzeResponse(String targetRole, Integer matchPercentage, List<String> extractedSkills, List<String> missingSkills, List<String> recommendedTopics, String detailedSummary) {
        this.targetRole = targetRole;
        this.matchPercentage = matchPercentage;
        this.extractedSkills = extractedSkills;
        this.missingSkills = missingSkills;
        this.recommendedTopics = recommendedTopics;
        this.detailedSummary = detailedSummary;
    }

    public String getTargetRole() { return targetRole; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }

    public Integer getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(Integer matchPercentage) { this.matchPercentage = matchPercentage; }

    public List<String> getExtractedSkills() { return extractedSkills; }
    public void setExtractedSkills(List<String> extractedSkills) { this.extractedSkills = extractedSkills; }

    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }

    public List<String> getRecommendedTopics() { return recommendedTopics; }
    public void setRecommendedTopics(List<String> recommendedTopics) { this.recommendedTopics = recommendedTopics; }

    public String getDetailedSummary() { return detailedSummary; }
    public void setDetailedSummary(String detailedSummary) { this.detailedSummary = detailedSummary; }
}
