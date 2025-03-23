package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;

public class RemoveQuestionFromQuizCommand implements Command<Void> {
    private IdsCourse idsCourse;

    public RemoveQuestionFromQuizCommand(IdsCourse idsCourse) {
        this.idsCourse = idsCourse;
    }

    public IdsCourse getIdsCourse() {
        return idsCourse;
    }
}
