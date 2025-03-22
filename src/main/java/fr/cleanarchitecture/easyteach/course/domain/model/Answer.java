package fr.cleanarchitecture.easyteach.course.domain.model;

import java.util.UUID;

public class Answer {
    private String answerId;
    private String answerText;
    private boolean isCorrect;

    public Answer(String answerText, boolean isCorrect) {
        this.answerId = UUID.randomUUID().toString();
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }

    public String getAnswerId() {
        return answerId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
