package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;

public class DetachQuizFromLessonCommand implements Command<Void> {
    private IdsCourse idsCourse;

    public DetachQuizFromLessonCommand(IdsCourse idsCourse) {
        this.idsCourse = idsCourse;
    }

    public IdsCourse getIdsCourse() {
        return idsCourse;
    }
}
