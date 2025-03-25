package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Quiz;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

public class AttachQuizToLessonCommand implements Command<BaseViewModel<Lesson>> {
    private IdsCourse idsCourse;
    private Quiz quiz;

    public AttachQuizToLessonCommand(IdsCourse idsCourse, Quiz quiz) {
        this.idsCourse = idsCourse;
        this.quiz = quiz;
    }

    public IdsCourse getIdsCourse() {
        return idsCourse;
    }

    public Quiz getQuiz() {
        return quiz;
    }
}
