package com.aiskill.platform.controller;

import com.aiskill.platform.dto.ResumeAnalyzeRequest;
import com.aiskill.platform.dto.ResumeAnalyzeResponse;
import com.aiskill.platform.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume")
@Tag(name = "Resume Analyzer", description = "AI Resume Evaluation & Skill Matching APIs")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/analyze")
    @Operation(summary = "Analyze resume text against target career benchmarks and return skill gaps")
    public ResponseEntity<ResumeAnalyzeResponse> analyzeResume(@RequestBody ResumeAnalyzeRequest request) {
        return ResponseEntity.ok(resumeService.analyzeResume(request));
    }
}
