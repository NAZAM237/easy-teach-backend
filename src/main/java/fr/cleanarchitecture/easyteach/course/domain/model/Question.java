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

    public Question(String questionText, QuestionType questionType, List<Answer> answers) {
        this.questionId = UUID.randomUUID().toString();
        this.questionText = questionText;
        this.questionType = questionType;
        this.answers = answers;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void updateData(Question newQuestion) {
        this.questionText = newQuestion.getQuestionText();
        this.questionType = newQuestion.getQuestionType();
        this.answers = newQuestion.getAnswers();
    }
}
