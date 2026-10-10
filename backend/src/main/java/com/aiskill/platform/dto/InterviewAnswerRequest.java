package com.aiskill.platform.dto;

public class InterviewAnswerRequest {
    private Long questionId;
    private String answer;

    public InterviewAnswerRequest() {}

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}
