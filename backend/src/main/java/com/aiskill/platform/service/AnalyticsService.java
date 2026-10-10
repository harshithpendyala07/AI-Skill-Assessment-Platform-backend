package com.aiskill.platform.service;

import com.aiskill.platform.dto.DashboardSummaryResponse;
import com.aiskill.platform.model.Interview;
import com.aiskill.platform.model.Result;
import com.aiskill.platform.model.Submission;
import com.aiskill.platform.model.User;
import com.aiskill.platform.repository.InterviewRepository;
import com.aiskill.platform.repository.ResultRepository;
import com.aiskill.platform.repository.SubmissionRepository;
import com.aiskill.platform.repository.TestRepository;
import com.aiskill.platform.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AnalyticsService {
    private final ResultRepository resultRepository;
    private final InterviewRepository interviewRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final TestRepository testRepository;
    private final AuthService authService;

    public AnalyticsService(ResultRepository resultRepository,
                            InterviewRepository interviewRepository,
                            SubmissionRepository submissionRepository,
                            UserRepository userRepository,
                            TestRepository testRepository,
                            AuthService authService) {
        this.resultRepository = resultRepository;
        this.interviewRepository = interviewRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
        this.testRepository = testRepository;
        this.authService = authService;
    }

    public DashboardSummaryResponse getDashboardSummary() {
        User user = authService.getCurrentAuthenticatedUser();
        Long userId = user.getId();

        Long testsCount = resultRepository.countTestsTakenByUserId(userId);
        Double avgScore = resultRepository.findAverageScoreByUserId(userId);
        Long interviewsCount = interviewRepository.countByUserId(userId);
        long totalAssessments = testRepository.count();
        long completedAssessments = testsCount != null ? testsCount : 0L;
        double participation = totalAssessments == 0 ? 0.0 :
                Math.min(100.0, (completedAssessments * 100.0) / totalAssessments);

        List<Submission> userSubmissions = submissionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        long solvedProblems = userSubmissions.stream()
                .filter(s -> "Accepted".equalsIgnoreCase(s.getStatus()))
                .map(s -> s.getProblem().getId())
                .distinct()
                .count();

        DashboardSummaryResponse response = new DashboardSummaryResponse();
        response.setTestsTaken(completedAssessments);
        response.setTotalAssessments(totalAssessments);
        response.setCompletedAssessments(completedAssessments);
        response.setParticipationPercentage(round(participation));
        response.setAverageScore(avgScore != null ? round(avgScore) : 0.0);
        response.setInterviewsAttended(interviewsCount != null ? interviewsCount : 0L);
        response.setProblemsSolved(solvedProblems);
        response.setCurrentRank(avgScore != null && avgScore >= 85 ? "High Performer"
                : avgScore != null && avgScore >= 70 ? "Consistent Learner" : "Getting Started");

        Map<String, Integer> mastery = new LinkedHashMap<>();
        Map<String, List<Integer>> skillScores = new LinkedHashMap<>();
        for (Result r : resultRepository.findByUserIdOrderByCreatedAtDesc(userId)) {
            skillScores.computeIfAbsent(r.getTest().getSkill(), k -> new ArrayList<>()).add(r.getScore());
        }
        for (Map.Entry<String, List<Integer>> e : skillScores.entrySet()) {
            int avg = (int) Math.round(e.getValue().stream().mapToInt(Integer::intValue).average().orElse(0));
            mastery.put(e.getKey(), avg);
        }
        response.setSkillMastery(mastery);

        List<DashboardSummaryResponse.RecentActivityDto> activities = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

        for (Result r : resultRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().limit(5).toList()) {
            activities.add(new DashboardSummaryResponse.RecentActivityDto(
                    "TEST", r.getTest().getTitle(), r.getScore(), r.getCreatedAt().format(formatter)));
        }
        for (Interview i : interviewRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().limit(2).toList()) {
            activities.add(new DashboardSummaryResponse.RecentActivityDto(
                    "INTERVIEW", "AI Mock Interview - " + i.getSkill(),
                    i.getScore() != null ? i.getScore() : 0, i.getCreatedAt().format(formatter)));
        }
        response.setRecentActivities(activities);
        return response;
    }

    public List<Map<String, Object>> getLeaderboard() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (User u : userRepository.findAll()) {
            if (u.getRole() == com.aiskill.platform.model.Role.ROLE_ADMIN) continue;
            Double avg = resultRepository.findAverageScoreByUserId(u.getId());
            Long tests = resultRepository.countTestsTakenByUserId(u.getId());
            Map<String, Object> map = new HashMap<>();
            map.put("name", u.getName());
            map.put("email", u.getEmail());
            map.put("targetRole", u.getTargetRole());
            map.put("avgScore", avg != null ? round(avg) : 0.0);
            map.put("testsTaken", tests != null ? tests : 0L);
            map.put("points", avg != null ? (int) Math.round(avg * 10) : 0);
            list.add(map);
        }
        list.sort((a, b) -> Double.compare(((Number)b.get("avgScore")).doubleValue(),
                ((Number)a.get("avgScore")).doubleValue()));
        int rank = 1;
        for (Map<String, Object> item : list) item.put("rank", rank++);
        return list;
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
