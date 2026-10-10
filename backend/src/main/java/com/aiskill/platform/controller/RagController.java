package com.aiskill.platform.controller;

import com.aiskill.platform.dto.RagQueryRequest;
import com.aiskill.platform.dto.RagQueryResponse;
import com.aiskill.platform.model.StudyDocument;
import com.aiskill.platform.service.RagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rag")
@Tag(name = "AI Study Assistant (RAG)", description = "Retrieval-Augmented Generation Study APIs")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/ask")
    @Operation(summary = "Ask question to AI Study Assistant grounded by indexed knowledge base")
    public ResponseEntity<RagQueryResponse> askAssistant(@RequestBody RagQueryRequest request) {
        return ResponseEntity.ok(ragService.queryAssistant(request));
    }

    @PostMapping("/upload")
    @Operation(summary = "Upload and chunk study material into the RAG knowledge base")
    public ResponseEntity<StudyDocument> uploadDocument(@RequestBody Map<String, String> payload) {
        String title = payload.getOrDefault("title", "Untitled Document");
        String category = payload.getOrDefault("category", "General");
        String content = payload.getOrDefault("content", "");
        return ResponseEntity.ok(ragService.uploadDocument(title, category, content));
    }

    @GetMapping("/documents")
    @Operation(summary = "List all indexed study documents")
    public ResponseEntity<List<StudyDocument>> getDocuments() {
        return ResponseEntity.ok(ragService.getAllDocuments());
    }
}
