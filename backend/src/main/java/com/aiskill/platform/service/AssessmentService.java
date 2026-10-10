package com.aiskill.platform.service;

import com.aiskill.platform.dto.TestSubmitRequest;
import com.aiskill.platform.dto.TestSubmitResponse;
import com.aiskill.platform.model.Question;
import com.aiskill.platform.model.Result;
import com.aiskill.platform.model.Test;
import com.aiskill.platform.model.User;
import com.aiskill.platform.repository.QuestionRepository;
import com.aiskill.platform.repository.ResultRepository;
import com.aiskill.platform.repository.TestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AssessmentService {

    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;
    private final AuthService authService;

    public AssessmentService(TestRepository testRepository, QuestionRepository questionRepository,
                             ResultRepository resultRepository, AuthService authService) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
        this.authService = authService;
    }

    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    public Test getTestById(Long id) {
        return testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Test not found with ID: " + id));
    }

    @Transactional
    public TestSubmitResponse submitTest(Long testId, TestSubmitRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        Test test = getTestById(testId);
        List<Question> questions = questionRepository.findByTestId(testId);

        int totalQuestions = questions.size();
        int correctCount = 0;
        int wrongCount = 0;
        List<TestSubmitResponse.QuestionDetail> details = new ArrayList<>();
        Map<Long, String> answers = request.getAnswers() != null ? request.getAnswers() : Map.of();

        for (Question q : questions) {
            String selected = answers.get(q.getId());
            boolean isCorrect = selected != null && selected.trim().equalsIgnoreCase(q.getAnswer().trim());
            if (isCorrect) {
                correctCount++;
            } else {
                wrongCount++;
            }

            details.add(new TestSubmitResponse.QuestionDetail(
                    q.getId(),
                    q.getQuestion(),
                    selected != null ? selected : "Not Answered",
                    q.getAnswer(),
                    isCorrect,
                    q.getExplanation()
            ));
        }

        int score = totalQuestions > 0 ? (int) Math.round(((double) correctCount / totalQuestions) * 100) : 0;
        String feedback = generateFeedback(score, test.getSkill(), test.getDifficulty(), correctCount, totalQuestions);

        Result result = new Result(
                user,
                test,
                score,
                correctCount,
                wrongCount,
                totalQuestions,
                request.getTimeSpentSeconds() != null ? request.getTimeSpentSeconds() : 0,
                feedback
        );
        result = resultRepository.save(result);

        TestSubmitResponse response = new TestSubmitResponse();
        response.setResultId(result.getId());
        response.setScore(score);
        response.setCorrectCount(correctCount);
        response.setWrongCount(wrongCount);
        response.setTotalQuestions(totalQuestions);
        response.setFeedback(feedback);
        response.setQuestionDetails(details);

        return response;
    }

    private String generateFeedback(int score, String skill, String difficulty, int correct, int total) {
        if (score >= 90) {
            return String.format("Outstanding performance! You mastered %s at the %s level with an impressive %d/%d (%d%%). You have a solid grasp of foundational and advanced architectural concepts.",
                    skill, difficulty, correct, total, score);
        } else if (score >= 70) {
            return String.format("Good job! You scored %d%% in %s (%s). You have a strong foundation, but review the explanations on edge cases and advanced internals to push toward mastery.",
                    score, skill, difficulty);
        } else if (score >= 50) {
            return String.format("Fair effort with %d%% in %s. We recommend revisiting the key concepts and taking advantage of the AI Study Assistant to clarify the topics you missed.",
                    score, skill);
        } else {
            return String.format("You scored %d%% in %s. Foundational concepts need reinforcement. Check the recommended study guide in the Study Assistant module and retry the assessment.",
                    score, skill);
        }
    }
}
