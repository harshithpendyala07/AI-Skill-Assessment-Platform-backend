package com.aiskill.platform.controller;

import com.aiskill.platform.dto.TestSubmitRequest;
import com.aiskill.platform.dto.TestSubmitResponse;
import com.aiskill.platform.model.Test;
import com.aiskill.platform.service.AssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
@Tag(name = "Skill Assessments", description = "MCQ Assessments and Evaluation APIs")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @GetMapping
    @Operation(summary = "List all available skill tests")
    public ResponseEntity<List<Test>> getAllTests() {
        return ResponseEntity.ok(assessmentService.getAllTests());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specific test details with questions")
    public ResponseEntity<Test> getTestById(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.getTestById(id));
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "Submit test answers for automated scoring and feedback")
    public ResponseEntity<TestSubmitResponse> submitTest(@PathVariable Long id, @RequestBody TestSubmitRequest request) {
        return ResponseEntity.ok(assessmentService.submitTest(id, request));
    }
}
