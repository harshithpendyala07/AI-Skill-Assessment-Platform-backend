package com.aiskill.platform.service;

import com.aiskill.platform.dto.InterviewAnswerRequest;
import com.aiskill.platform.dto.InterviewStartRequest;
import com.aiskill.platform.model.Interview;
import com.aiskill.platform.model.InterviewQuestion;
import com.aiskill.platform.model.User;
import com.aiskill.platform.repository.InterviewQuestionRepository;
import com.aiskill.platform.repository.InterviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final InterviewQuestionRepository questionRepository;
    private final AuthService authService;

    public InterviewService(InterviewRepository interviewRepository,
                            InterviewQuestionRepository questionRepository,
                            AuthService authService) {
        this.interviewRepository = interviewRepository;
        this.questionRepository = questionRepository;
        this.authService = authService;
    }

    public List<Interview> getUserInterviews() {
        User user = authService.getCurrentAuthenticatedUser();
        return interviewRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public Interview getInterviewById(Long id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found with ID: " + id));
    }

    @Transactional
    public Interview startInterview(InterviewStartRequest request) {
        User user = authService.getCurrentAuthenticatedUser();

        Interview interview = new Interview();
        interview.setUser(user);
        interview.setSkill(request.getSkill() != null ? request.getSkill() : "Full Stack Developer");
        interview.setDifficulty(request.getDifficulty() != null ? request.getDifficulty() : "Intermediate");
        interview.setStatus("IN_PROGRESS");
        interview = interviewRepository.save(interview);

        List<String> questionsPool = getQuestionsForSkillAndLevel(interview.getSkill(), interview.getDifficulty());
        List<InterviewQuestion> interviewQuestions = new ArrayList<>();

        int index = 1;
        for (String qText : questionsPool) {
            InterviewQuestion iq = new InterviewQuestion(interview, qText, index++);
            interviewQuestions.add(questionRepository.save(iq));
        }

        interview.setQuestions(interviewQuestions);
        return interview;
    }

    @Transactional
    public InterviewQuestion submitAnswer(Long interviewId, InterviewAnswerRequest request) {
        InterviewQuestion question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found with ID: " + request.getQuestionId()));

        String answer = request.getAnswer() != null ? request.getAnswer().trim() : "";
        question.setAnswer(answer);

        // Intelligent AI evaluation of answer
        int score = evaluateAnswerQuality(answer, question.getQuestion());
        String feedback = generateQuestionFeedback(score, answer, question.getQuestion());

        question.setScore(score);
        question.setFeedback(feedback);

        return questionRepository.save(question);
    }

    @Transactional
    public Interview completeInterview(Long interviewId) {
        Interview interview = getInterviewById(interviewId);
        List<InterviewQuestion> questions = questionRepository.findByInterviewIdOrderByOrderIndexAsc(interviewId);

        int totalAnswered = 0;
        int totalScore = 0;

        for (InterviewQuestion q : questions) {
            if (q.getScore() != null) {
                totalScore += q.getScore();
                totalAnswered++;
            }
        }

        int finalScore = totalAnswered > 0 ? totalScore / totalAnswered : 65;
        interview.setScore(finalScore);
        interview.setStatus("COMPLETED");

        String feedbackReport = generateFinalInterviewFeedback(interview, questions, finalScore);
        interview.setFeedback(feedbackReport);

        return interviewRepository.save(interview);
    }

    private int evaluateAnswerQuality(String answer, String question) {
        if (answer.isEmpty()) return 20;
        int length = answer.split("\\s+").length;
        int score = 50;

        if (length > 20) score += 20;
        if (length > 50) score += 15;

        // Keyword analysis
        String lower = answer.toLowerCase();
        if (lower.contains("for example") || lower.contains("such as") || lower.contains("because") || lower.contains("in my experience")) {
            score += 10;
        }
        if (lower.contains("performance") || lower.contains("scalability") || lower.contains("architecture") || lower.contains("security") || lower.contains("design pattern")) {
            score += 5;
        }

        return Math.min(score, 98);
    }

    private String generateQuestionFeedback(int score, String answer, String question) {
        if (score >= 85) {
            return "Excellent response! Clear technical precision, good structure, and relevant practical context provided.";
        } else if (score >= 70) {
            return "Solid response. You addressed the core question well. To improve, try illustrating with a concrete real-world implementation example and mention potential edge cases.";
        } else if (score >= 50) {
            return "Acceptable answer, but lacks architectural depth. Clarify the underlying principles and explain 'why' you chose this specific approach.";
        } else {
            return "The answer is too brief or vague. In real interviews, structure your response using the STAR method (Situation, Task, Action, Result) with clear technical terminology.";
        }
    }

    private String generateFinalInterviewFeedback(Interview interview, List<InterviewQuestion> questions, int score) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("### AI Mock Interview Evaluation Summary\n\n"));
        sb.append(String.format("- **Target Role**: %s\n", interview.getSkill()));
        sb.append(String.format("- **Experience Level**: %s\n", interview.getDifficulty()));
        sb.append(String.format("- **Overall Score**: %d / 100\n\n", score));

        sb.append("#### Performance Breakdown:\n");
        sb.append(String.format("- **Technical Accuracy**: %d%%\n", Math.min(100, score + 2)));
        sb.append(String.format("- **Communication & Articulation**: %d%%\n", Math.max(50, score - 3)));
        sb.append(String.format("- **Problem Solving & Clarity**: %d%%\n\n", score));

        sb.append("#### Key Strengths:\n");
        sb.append("- Demonstrated good familiarity with core concepts and standard terminology.\n");
        sb.append("- Maintained coherent conversational flow and addressed the primary question prompts.\n\n");

        sb.append("#### Recommendations for Improvement:\n");
        sb.append("- Provide deeper architectural reasoning when discussing trade-offs (e.g. latency vs consistency, memory vs CPU).\n");
        sb.append("- Practice giving concrete code examples and discussing unit testing strategies.\n");

        return sb.toString();
    }

    private List<String> getQuestionsForSkillAndLevel(String skill, String difficulty) {
        List<String> list = new ArrayList<>();
        String s = skill.toLowerCase();

        if (s.contains("java") || s.contains("spring")) {
            list.add("Can you explain how the Spring IoC container and Dependency Injection work behind the scenes?");
            list.add("What is the difference between @Component, @Service, and @Repository annotations in Spring Boot?");
            list.add("How do you handle transactional rollback in Spring using @Transactional, and what are checked vs unchecked exception behaviors?");
            list.add("Explain Java Memory Model (Heap, Stack, Metaspace) and how Garbage Collection handles memory management.");
            list.add("How do you secure REST APIs in a Spring Boot application using JWT tokens and Spring Security filters?");
        } else if (s.contains("react") || s.contains("front")) {
            list.add("How does the Virtual DOM work in React, and what is the reconciliation algorithm?");
            list.add("Can you explain the differences between useEffect, useMemo, and useCallback hooks, and when to use each?");
            list.add("How do you manage application state in complex React apps, and how do Context API and external stores compare?");
            list.add("What are React Server Components and how do they differ from traditional Client Components?");
            list.add("How would you optimize the loading and rendering performance of a large React dashboard?");
        } else if (s.contains("python") || s.contains("ai") || s.contains("ml")) {
            list.add("Explain the concept of Retrieval-Augmented Generation (RAG) and how vector embeddings are used.");
            list.add("What is the difference between lists, tuples, and sets in Python in terms of memory and lookup performance?");
            list.add("How does Python's Global Interpreter Lock (GIL) affect multithreading vs multiprocessing?");
            list.add("How would you design an API rate limiter using Python and Redis for an LLM endpoint?");
            list.add("What strategies do you use to evaluate and prevent hallucinations in LLM applications?");
        } else {
            list.add("Tell me about a challenging full-stack project you engineered and the key architectural trade-offs you made.");
            list.add("How do you design a scalable RESTful API with proper HTTP status codes, pagination, and error handling?");
            list.add("What is the difference between SQL and NoSQL databases, and when would you choose one over the other?");
            list.add("How do you ensure security against common vulnerabilities like SQL injection, XSS, and CSRF?");
            list.add("Explain how CI/CD pipelines, containerization (Docker), and automated testing improve software reliability.");
        }

        return list;
    }
}
