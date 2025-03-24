package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.domain.model.Quiz;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;

public class UpdateQuizFromLessonCommand implements Command<BaseViewModel<Quiz>> {
    private IdsCourse idsCourse;
    private Quiz quiz;

    public UpdateQuizFromLessonCommand(IdsCourse idsCourse, Quiz quiz) {
        this.idsCourse = idsCourse;
        this.quiz = quiz;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public IdsCourse getIdsCourse() {
        return idsCourse;
    }
}
