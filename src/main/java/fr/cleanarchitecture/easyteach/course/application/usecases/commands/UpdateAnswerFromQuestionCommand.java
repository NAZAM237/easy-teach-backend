package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.model.Answer;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

public class UpdateAnswerFromQuestionCommand implements Command<BaseViewModel<Answer>> {
    private IdsCourse idsCourse;
    private String answerText;
    private boolean correctAnswer;

    public UpdateAnswerFromQuestionCommand(IdsCourse idsCourse, String answerText, boolean correctAnswer) {
        this.idsCourse = idsCourse;
        this.answerText = answerText;
        this.correctAnswer = correctAnswer;
    }

    public IdsCourse getIdsCourse() {
        return idsCourse;
    }

    public String getAnswerText() {
        return answerText;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }
}
