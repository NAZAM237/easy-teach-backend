package fr.cleanarchitecture.easyteach.course.domain.model;

import java.util.Set;
import java.util.UUID;

public class Quiz {
    private String quizId;
    private String quizTitle;
    private String description;
    private Set<Question> questions;
    private int passingScore;

    public Quiz() {}

    public Quiz(String quizTitle, String description, Set<Question> questions, int passingScore) {
        this.quizId = UUID.randomUUID().toString();
        this.quizTitle = quizTitle;
        this.description = description;
        this.questions = questions;
        this.passingScore = passingScore;
    }

    public String getQuizId() {
        return quizId;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public String getDescription() {
        return description;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public int getPassingScore() {
        return passingScore;
    }
}
