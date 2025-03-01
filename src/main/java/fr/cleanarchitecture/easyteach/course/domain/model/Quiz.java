package fr.cleanarchitecture.easyteach.course.domain.model;

import java.util.Set;
import java.util.UUID;

public class Quiz {
    private String quizId;
    private String quizTitle;
    private Set<Question> questions;

    public Quiz() {}

    public Quiz(String quizTitle, Set<Question> questions) {
        this.quizId = UUID.randomUUID().toString();
        this.quizTitle = quizTitle;
        this.questions = questions;
    }
}
