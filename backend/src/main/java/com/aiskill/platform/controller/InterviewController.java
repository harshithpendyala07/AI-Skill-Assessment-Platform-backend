package com.aiskill.platform.controller;

import com.aiskill.platform.dto.InterviewAnswerRequest;
import com.aiskill.platform.dto.InterviewStartRequest;
import com.aiskill.platform.model.Interview;
import com.aiskill.platform.model.InterviewQuestion;
import com.aiskill.platform.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@Tag(name = "AI Mock Interview", description = "AI-Driven Mock Interview Session APIs")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping
    @Operation(summary = "Get user interview history")
    public ResponseEntity<List<Interview>> getUserInterviews() {
        return ResponseEntity.ok(interviewService.getUserInterviews());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get detailed interview session with questions and AI feedback")
    public ResponseEntity<Interview> getInterviewById(@PathVariable Long id) {
        return ResponseEntity.ok(interviewService.getInterviewById(id));
    }

    @PostMapping("/start")
    @Operation(summary = "Start a new AI mock interview session")
    public ResponseEntity<Interview> startInterview(@RequestBody InterviewStartRequest request) {
        return ResponseEntity.ok(interviewService.startInterview(request));
    }

    @PostMapping("/{id}/answer")
    @Operation(summary = "Submit candidate response to an interview question for AI evaluation")
    public ResponseEntity<InterviewQuestion> submitAnswer(@PathVariable Long id, @RequestBody InterviewAnswerRequest request) {
        return ResponseEntity.ok(interviewService.submitAnswer(id, request));
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Finalize mock interview and generate comprehensive performance report")
    public ResponseEntity<Interview> completeInterview(@PathVariable Long id) {
        return ResponseEntity.ok(interviewService.completeInterview(id));
    }
}
