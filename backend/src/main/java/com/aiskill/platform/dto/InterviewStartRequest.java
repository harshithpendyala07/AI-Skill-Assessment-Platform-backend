package com.aiskill.platform.dto;

public class InterviewStartRequest {
    private String skill; // e.g. "Full Stack Developer", "Java Spring Boot", "Frontend React", "Python AI/ML"
    private String difficulty; // "Junior", "Mid-Level", "Senior"

    public InterviewStartRequest() {}

    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}
