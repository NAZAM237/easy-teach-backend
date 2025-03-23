package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.domain.model.Question;
import fr.cleanarchitecture.easyteach.course.domain.model.Quiz;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;

public class AddQuestionToQuizCommand implements Command<BaseViewModel<Quiz>> {
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
