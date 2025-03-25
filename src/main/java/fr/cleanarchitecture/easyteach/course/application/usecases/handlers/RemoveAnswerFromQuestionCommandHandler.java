package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.RemoveAnswerFromQuestionCommand;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;

public class RemoveAnswerFromQuestionCommandHandler implements Command.Handler<RemoveAnswerFromQuestionCommand, Void> {

    private CourseRepository courseRepository;

    public RemoveAnswerFromQuestionCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Void handle(RemoveAnswerFromQuestionCommand removeAnswerFromQuestionCommand) {
        var course = courseRepository.findByCourseId(removeAnswerFromQuestionCommand.getIdsCourse().getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.removeAnswerFromQuestion(
                removeAnswerFromQuestionCommand.getIdsCourse().getModuleId(),
                removeAnswerFromQuestionCommand.getIdsCourse().getLessonId(),
                removeAnswerFromQuestionCommand.getIdsCourse().getQuestionId(),
                removeAnswerFromQuestionCommand.getIdsCourse().getAnswerId()
        );
        return null;
    }
}
