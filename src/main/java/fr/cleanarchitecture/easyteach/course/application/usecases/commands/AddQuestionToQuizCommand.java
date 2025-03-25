package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.model.Question;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

public class AddQuestionToQuizCommand implements Command<BaseViewModel<Question>> {
    private IdsCourse idsCourse;
    private Question question;

    public AddQuestionToQuizCommand(IdsCourse idsCourse, Question newQuestion) {
        this.idsCourse = idsCourse;
        this.question = newQuestion;
    }

    public IdsCourse getIdsCourse() {
        return idsCourse;
    }

    public Question getQuestion() {
        return question;
    }
}
