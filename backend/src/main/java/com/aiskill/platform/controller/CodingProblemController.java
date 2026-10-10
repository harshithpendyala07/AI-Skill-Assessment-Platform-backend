package com.aiskill.platform.controller;

import com.aiskill.platform.dto.CodeExecutionRequest;
import com.aiskill.platform.dto.CodeExecutionResponse;
import com.aiskill.platform.model.CodingProblem;
import com.aiskill.platform.service.CodingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coding-problems")
@Tag(name = "Coding Arena", description = "Coding Challenges and Execution APIs")
public class CodingProblemController {

    private final CodingService codingService;

    public CodingProblemController(CodingService codingService) {
        this.codingService = codingService;
    }

    @GetMapping
    @Operation(summary = "List all coding problems")
    public ResponseEntity<List<CodingProblem>> getAllProblems() {
        return ResponseEntity.ok(codingService.getAllProblems());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specific coding problem statement")
    public ResponseEntity<CodingProblem> getProblemById(@PathVariable Long id) {
        return ResponseEntity.ok(codingService.getProblemById(id));
    }

    @PostMapping("/run")
    @Operation(summary = "Run code against sample test cases")
    public ResponseEntity<CodeExecutionResponse> runCode(@RequestBody CodeExecutionRequest request) {
        return ResponseEntity.ok(codingService.runCode(request));
    }

    @PostMapping("/submit")
    @Operation(summary = "Submit code solution for complete grading and save submission")
    public ResponseEntity<CodeExecutionResponse> submitCode(@RequestBody CodeExecutionRequest request) {
        return ResponseEntity.ok(codingService.submitCode(request));
    }
}
