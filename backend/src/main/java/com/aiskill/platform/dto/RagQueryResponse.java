package com.aiskill.platform.dto;

import java.util.List;

public class RagQueryResponse {
    private String answer;
    private List<SourceChunk> sources;

    public static class SourceChunk {
        private String documentTitle;
        private String category;
        private String snippet;

        public SourceChunk() {}

        public SourceChunk(String documentTitle, String category, String snippet) {
            this.documentTitle = documentTitle;
            this.category = category;
            this.snippet = snippet;
        }

        public String getDocumentTitle() { return documentTitle; }
        public void setDocumentTitle(String documentTitle) { this.documentTitle = documentTitle; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public String getSnippet() { return snippet; }
        public void setSnippet(String snippet) { this.snippet = snippet; }
    }

    public RagQueryResponse() {}

    public RagQueryResponse(String answer, List<SourceChunk> sources) {
        this.answer = answer;
        this.sources = sources;
    }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public List<SourceChunk> getSources() { return sources; }
    public void setSources(List<SourceChunk> sources) { this.sources = sources; }
}
