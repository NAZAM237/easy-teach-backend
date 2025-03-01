package fr.cleanarchitecture.easyteach.course.domain.model;

import fr.cleanarchitecture.easyteach.course.domain.enums.QuestionType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Question {
    private String questionId;
    private String questionText;
    private QuestionType questionType;
    private List<Answer> answers = new ArrayList<>();

    public Question(String questionText, QuestionType questionType) {
        this.questionId = UUID.randomUUID().toString();
        this.questionText = questionText;
        this.questionType = questionType;
    }
}
