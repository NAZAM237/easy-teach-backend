package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.model.Question;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

public class UpdateQuestionFromQuizCommand implements Command<BaseViewModel<Question>> {
    private IdsCourse idsCourse;
    private Question question;

    public UpdateQuestionFromQuizCommand(IdsCourse idsCourse, Question question) {
        this.idsCourse = idsCourse;
        this.question = question;
    }

    public IdsCourse getIdsCourse() {
        return idsCourse;
    }

    public Question getQuestion() {
        return question;
    }
}
