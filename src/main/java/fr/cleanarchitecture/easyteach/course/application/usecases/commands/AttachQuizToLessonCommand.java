package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.model.Quiz;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;

public class AttachQuizToLessonCommand implements Command<Void> {
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
