package com.aiskill.platform.controller;

import com.aiskill.platform.model.CodingProblem;
import com.aiskill.platform.model.Role;
import com.aiskill.platform.model.Test;
import com.aiskill.platform.model.User;
import com.aiskill.platform.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(name = "Admin Operations", description = "Administrative Management APIs")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
    private final UserRepository userRepository;
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final CodingProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;
    private final InterviewRepository interviewRepository;
    private final ResultRepository resultRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;

    public AdminController(UserRepository userRepository,
                           TestRepository testRepository,
                           QuestionRepository questionRepository,
                           CodingProblemRepository problemRepository,
                           SubmissionRepository submissionRepository,
                           InterviewRepository interviewRepository,
                           ResultRepository resultRepository,
                           ResumeAnalysisRepository resumeAnalysisRepository) {
        this.userRepository = userRepository;
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.problemRepository = problemRepository;
        this.submissionRepository = submissionRepository;
        this.interviewRepository = interviewRepository;
        this.resultRepository = resultRepository;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
    }

    @GetMapping("/users")
    @Operation(summary = "List all registered users without password fields")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<Map<String, Object>> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", user.getId());
            item.put("name", user.getName());
            item.put("email", user.getEmail());
            item.put("role", user.getRole());
            item.put("targetRole", user.getTargetRole());
            item.put("createdAt", user.getCreatedAt());
            users.add(item);
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/students")
    @Operation(summary = "List students with real performance and participation")
    public ResponseEntity<List<Map<String, Object>>> getStudents() {
        long totalAssessments = testRepository.count();
        List<Map<String, Object>> students = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            if (user.getRole() != Role.ROLE_STUDENT) continue;

            List<com.aiskill.platform.model.Result> results =
                    resultRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
            Double avg = resultRepository.findAverageScoreByUserId(user.getId());
            long completed = results.stream().map(r -> r.getTest().getId()).distinct().count();
            double participation = totalAssessments == 0 ? 0.0 :
                    Math.min(100.0, completed * 100.0 / totalAssessments);

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", user.getId());
            item.put("name", user.getName());
            item.put("email", user.getEmail());
            item.put("targetRole", user.getTargetRole());
            item.put("createdAt", user.getCreatedAt());
            item.put("completedAssessments", completed);
            item.put("totalAssessments", totalAssessments);
            item.put("participationPercentage", round(participation));
            item.put("averagePercentage", avg != null ? round(avg) : 0.0);
            item.put("lastActivity", results.isEmpty() ? null : results.get(0).getCreatedAt());
            students.add(item);
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/students/{id}")
    @Operation(summary = "Get a student's assignment history and performance details")
    public ResponseEntity<Map<String, Object>> getStudentDetails(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));
        if (user.getRole() != Role.ROLE_STUDENT) {
            throw new IllegalArgumentException("Only student details are available here.");
        }

        List<com.aiskill.platform.model.Result> results =
                resultRepository.findByUserIdOrderByCreatedAtDesc(id);
        long totalAssessments = testRepository.count();
        long completed = results.stream().map(r -> r.getTest().getId()).distinct().count();
        Double avg = resultRepository.findAverageScoreByUserId(id);

        List<Map<String, Object>> history = new ArrayList<>();
        for (com.aiskill.platform.model.Result r : results) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("assignmentId", r.getTest().getId());
            row.put("assignment", r.getTest().getTitle());
            row.put("skill", r.getTest().getSkill());
            row.put("percentage", r.getScore());
            row.put("correct", r.getCorrectCount());
            row.put("totalQuestions", r.getTotalQuestions());
            row.put("completedAt", r.getCreatedAt());
            history.add(row);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("targetRole", user.getTargetRole());
        response.put("averagePercentage", avg != null ? round(avg) : 0.0);
        response.put("completedAssessments", completed);
        response.put("totalAssessments", totalAssessments);
        response.put("participationPercentage", totalAssessments == 0 ? 0.0 :
                round(Math.min(100.0, completed * 100.0 / totalAssessments)));
        response.put("history", history);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get platform-wide statistics")
    public ResponseEntity<Map<String, Object>> getPlatformStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        long students = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.ROLE_STUDENT).count();
        long totalAssessments = testRepository.count();
        long completed = resultRepository.count();
        Double avg = resultRepository.findOverallAverageScore();

        double participation = 0.0;
        if (students > 0 && totalAssessments > 0) {
            long distinctStudentTests = resultRepository.findAll().stream()
                    .map(r -> r.getUser().getId() + ":" + r.getTest().getId())
                    .distinct().count();
            participation = Math.min(100.0, distinctStudentTests * 100.0 / (students * totalAssessments));
        }

        stats.put("totalUsers", userRepository.count());
        stats.put("totalStudents", students);
        stats.put("totalTests", totalAssessments);
        stats.put("totalQuestions", questionRepository.count());
        stats.put("totalCodingProblems", problemRepository.count());
        stats.put("totalSubmissions", submissionRepository.count());
        stats.put("totalInterviews", interviewRepository.count());
        stats.put("totalResults", completed);
        stats.put("averagePercentage", avg != null ? round(avg) : 0.0);
        stats.put("participationPercentage", round(participation));
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/tests")
    @Operation(summary = "Create an assessment with administrator-entered questions")
    public ResponseEntity<Test> createTest(@RequestBody Test test) {
        if (test.getQuestions() == null || test.getQuestions().isEmpty()) {
            throw new IllegalArgumentException("Add at least one question before creating the assessment.");
        }
        test.getQuestions().forEach(q -> {
            q.setTest(test);
            if (q.getAnswer() == null || !q.getAnswer().matches("(?i)[ABCD]")) {
                throw new IllegalArgumentException("Each question must have a correct answer of A, B, C, or D.");
            }
        });
        return ResponseEntity.ok(testRepository.save(test));
    }

    @DeleteMapping("/tests/{id}")
    @Operation(summary = "Delete an assessment")
    public ResponseEntity<Map<String, String>> deleteTest(@PathVariable Long id) {
        testRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Assessment deleted successfully."));
    }

    @DeleteMapping("/users/{id}/history")
    @Transactional
    @Operation(summary = "Clear a student's assessment and activity history")
    public ResponseEntity<Map<String, String>> clearStudentHistory(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));
        if (user.getRole() != Role.ROLE_STUDENT) {
            throw new IllegalArgumentException("Only student history can be cleared.");
        }

        resultRepository.deleteByUserId(id);
        submissionRepository.deleteByUserId(id);
        resumeAnalysisRepository.deleteByUserId(id);
        interviewRepository.deleteByUserId(id);

        return ResponseEntity.ok(Map.of("message", "Student history cleared successfully."));
    }

    @DeleteMapping("/users/{id}")
    @Transactional
    @Operation(summary = "Delete a student account and its history")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));
        if (user.getRole() != Role.ROLE_STUDENT) {
            throw new IllegalArgumentException("Only student accounts can be deleted.");
        }

        resultRepository.deleteByUserId(id);
        submissionRepository.deleteByUserId(id);
        resumeAnalysisRepository.deleteByUserId(id);
        interviewRepository.deleteByUserId(id);
        userRepository.delete(user);

        return ResponseEntity.ok(Map.of("message", "Student account deleted successfully."));
    }

    @PostMapping("/coding-problems")
    @Operation(summary = "Create a new coding problem")
    public ResponseEntity<CodingProblem> createCodingProblem(@RequestBody CodingProblem problem) {
        return ResponseEntity.ok(problemRepository.save(problem));
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
