package fr.cleanarchitecture.easyteach.course.domain.model;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Quiz {
    private String quizId;
    private String quizTitle;
    private String description;
    private Set<Question> questions = new HashSet<>();
    private int passingScore;

    public Quiz() {}

    public Quiz(String quizTitle, String description, int passingScore) {
        this.quizId = UUID.randomUUID().toString();
        this.quizTitle = quizTitle;
        this.description = description;
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

    public void addQuestion(Question question) {
        var isExist = questions.stream().noneMatch(question1 -> question1.getQuestionText().equals(question.getQuestionText()));
        if (!isExist) {
            throw new BadRequestException("This question already exists in this quiz");
        }
        this.questions.add(question);
    }

    public void removeQuestion(String questionId) {
        var question = this.questions.stream()
                .filter(question1 -> question1.getQuestionId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("This question does not exist in this quiz"));

        this.questions.remove(question);
    }

    public void updateQuestionFromQuiz(String questionId, Question newQuestion) {
        var question = this.questions.stream()
                .filter(q -> q.getQuestionId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Question not found"));
        question.updateData(newQuestion);
    }
}
