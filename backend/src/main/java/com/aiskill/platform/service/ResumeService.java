package com.aiskill.platform.service;

import com.aiskill.platform.dto.ResumeAnalyzeRequest;
import com.aiskill.platform.dto.ResumeAnalyzeResponse;
import com.aiskill.platform.model.ResumeAnalysis;
import com.aiskill.platform.model.User;
import com.aiskill.platform.repository.ResumeAnalysisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ResumeService {

    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final AuthService authService;

    public ResumeService(ResumeAnalysisRepository resumeAnalysisRepository, AuthService authService) {
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.authService = authService;
    }

    @Transactional
    public ResumeAnalyzeResponse analyzeResume(ResumeAnalyzeRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        String resume = request.getResumeText() != null ? request.getResumeText().toLowerCase() : "";
        String role = request.getTargetRole() != null ? request.getTargetRole() : "Full Stack Developer";

        List<String> requiredSkills = getRequiredSkillsForRole(role);
        List<String> extractedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (String skill : requiredSkills) {
            if (resume.contains(skill.toLowerCase())) {
                extractedSkills.add(skill);
            } else {
                missingSkills.add(skill);
            }
        }

        int total = requiredSkills.size();
        int matched = extractedSkills.size();
        int matchPercentage = total > 0 ? (int) Math.round(((double) matched / total) * 100) : 50;

        List<String> recommendations = new ArrayList<>();
        for (String missing : missingSkills) {
            recommendations.add("Complete the " + missing + " skill assessment & coding problems to boost qualification.");
        }
        if (recommendations.isEmpty()) {
            recommendations.add("Your profile strongly matches standard benchmarks! Proceed with senior AI Mock Interview sessions.");
        }

        String summary = String.format(
                "Resume evaluated against industry benchmarks for %s. Found %d of %d key competencies (%d%% profile match). Focus on strengthening: %s.",
                role, matched, total, matchPercentage, String.join(", ", missingSkills.stream().limit(3).toList())
        );

        ResumeAnalysis analysis = new ResumeAnalysis(
                user,
                role,
                matchPercentage,
                String.join(", ", extractedSkills),
                String.join(", ", missingSkills),
                summary
        );
        resumeAnalysisRepository.save(analysis);

        return new ResumeAnalyzeResponse(
                role,
                matchPercentage,
                extractedSkills,
                missingSkills,
                recommendations,
                summary
        );
    }

    private List<String> getRequiredSkillsForRole(String role) {
        String r = role.toLowerCase();
        if (r.contains("back") || r.contains("java")) {
            return List.of("Java", "Spring Boot", "REST API", "SQL", "Hibernate", "Microservices", "Docker", "Git", "JUnit");
        } else if (r.contains("front") || r.contains("react")) {
            return List.of("JavaScript", "TypeScript", "React", "HTML5", "CSS3", "Redux", "Tailwind", "REST API", "Git");
        } else if (r.contains("ai") || r.contains("data") || r.contains("ml")) {
            return List.of("Python", "Machine Learning", "PyTorch", "TensorFlow", "SQL", "Data Analysis", "RAG", "Docker", "Git");
        } else {
            return List.of("Java", "Spring Boot", "React", "SQL", "REST API", "Git", "Docker", "Data Structures", "System Design");
        }
    }
}
