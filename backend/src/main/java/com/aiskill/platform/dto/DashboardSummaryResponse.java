package com.aiskill.platform.dto;

import java.util.List;
import java.util.Map;

public class DashboardSummaryResponse {
    private Long testsTaken;
    private Long totalAssessments;
    private Long completedAssessments;
    private Double participationPercentage;
    private Double averageScore;
    private Long interviewsAttended;
    private Long problemsSolved;
    private String currentRank;
    private Map<String, Integer> skillMastery;
    private List<RecentActivityDto> recentActivities;

    public static class RecentActivityDto {
        private String type;
        private String title;
        private Integer score;
        private String date;

        public RecentActivityDto() {}

        public RecentActivityDto(String type, String title, Integer score, String date) {
            this.type = type;
            this.title = title;
            this.score = score;
            this.date = date;
        }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Integer getScore() { return score; }
        public void setScore(Integer score) { this.score = score; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
    }

    public DashboardSummaryResponse() {}

    public Long getTestsTaken() { return testsTaken; }
    public void setTestsTaken(Long testsTaken) { this.testsTaken = testsTaken; }
    public Long getTotalAssessments() { return totalAssessments; }
    public void setTotalAssessments(Long totalAssessments) { this.totalAssessments = totalAssessments; }
    public Long getCompletedAssessments() { return completedAssessments; }
    public void setCompletedAssessments(Long completedAssessments) { this.completedAssessments = completedAssessments; }
    public Double getParticipationPercentage() { return participationPercentage; }
    public void setParticipationPercentage(Double participationPercentage) { this.participationPercentage = participationPercentage; }
    public Double getAverageScore() { return averageScore; }
    public void setAverageScore(Double averageScore) { this.averageScore = averageScore; }
    public Long getInterviewsAttended() { return interviewsAttended; }
    public void setInterviewsAttended(Long interviewsAttended) { this.interviewsAttended = interviewsAttended; }
    public Long getProblemsSolved() { return problemsSolved; }
    public void setProblemsSolved(Long problemsSolved) { this.problemsSolved = problemsSolved; }
    public String getCurrentRank() { return currentRank; }
    public void setCurrentRank(String currentRank) { this.currentRank = currentRank; }
    public Map<String, Integer> getSkillMastery() { return skillMastery; }
    public void setSkillMastery(Map<String, Integer> skillMastery) { this.skillMastery = skillMastery; }
    public List<RecentActivityDto> getRecentActivities() { return recentActivities; }
    public void setRecentActivities(List<RecentActivityDto> recentActivities) { this.recentActivities = recentActivities; }
}
