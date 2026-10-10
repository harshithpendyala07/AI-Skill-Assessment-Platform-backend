package com.aiskill.platform.dto;

import java.util.List;

public class TestSubmitResponse {
    private Long resultId;
    private Integer score;
    private Integer correctCount;
    private Integer wrongCount;
    private Integer totalQuestions;
    private String feedback;
    private List<QuestionDetail> questionDetails;

    public static class QuestionDetail {
        private Long questionId;
        private String question;
        private String selectedAnswer;
        private String correctAnswer;
        private boolean isCorrect;
        private String explanation;

        public QuestionDetail() {}

        public QuestionDetail(Long questionId, String question, String selectedAnswer, String correctAnswer, boolean isCorrect, String explanation) {
            this.questionId = questionId;
            this.question = question;
            this.selectedAnswer = selectedAnswer;
            this.correctAnswer = correctAnswer;
            this.isCorrect = isCorrect;
            this.explanation = explanation;
        }

        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }

        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }

        public String getSelectedAnswer() { return selectedAnswer; }
        public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer = selectedAnswer; }

        public String getCorrectAnswer() { return correctAnswer; }
        public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

        public boolean isCorrect() { return isCorrect; }
        public void setCorrect(boolean correct) { isCorrect = correct; }

        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }
    }

    public TestSubmitResponse() {}

    public Long getResultId() { return resultId; }
    public void setResultId(Long resultId) { this.resultId = resultId; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public Integer getCorrectCount() { return correctCount; }
    public void setCorrectCount(Integer correctCount) { this.correctCount = correctCount; }

    public Integer getWrongCount() { return wrongCount; }
    public void setWrongCount(Integer wrongCount) { this.wrongCount = wrongCount; }

    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public List<QuestionDetail> getQuestionDetails() { return questionDetails; }
    public void setQuestionDetails(List<QuestionDetail> questionDetails) { this.questionDetails = questionDetails; }
}
