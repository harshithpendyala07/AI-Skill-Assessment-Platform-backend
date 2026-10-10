package com.aiskill.platform.dto;

import java.util.List;

public class RagQueryRequest {
    private String query;
    private String category; // optional filter

    public RagQueryRequest() {}

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
