package fr.cleanarchitecture.easyteach.course.application.usecases.commands;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.domain.model.Answer;
import fr.cleanarchitecture.easyteach.course.domain.model.Question;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

public class AddAnswerToQuestionCommand implements Command<BaseViewModel<Question>> {
    private IdsCourse idsCourse;
    private Answer answer;

    public AddAnswerToQuestionCommand(IdsCourse idsCourse, Answer answer) {
        this.idsCourse = idsCourse;
        this.answer = answer;
    }

    public IdsCourse getIdsCourse() {
        return idsCourse;
    }

    public Answer getAnswer() {
        return answer;
    }
}
