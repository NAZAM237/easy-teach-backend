package fr.cleanarchitecture.easyteach.course.domain.model;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
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

    public void addAnswer(Answer answer) {
        var noPresent = this.answers.stream().noneMatch(a -> a.getAnswerText().equals(answer.getAnswerText()));
        if (!noPresent) {
            throw new BadRequestException("Answer already exists in this question");
        }
        this.answers.add(answer);
    }

    public void updateAnswer(String answerId, Answer newAnswer) {
        var answer = this.answers.stream().filter(a -> a.getAnswerId().equals(answerId))
                .findFirst().orElseThrow(() -> new NotFoundException("Answer not found for this question"));
        answer.updateData(newAnswer);
    }
}
