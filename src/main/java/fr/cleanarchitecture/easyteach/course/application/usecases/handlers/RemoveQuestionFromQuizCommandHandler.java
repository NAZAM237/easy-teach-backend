package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.RemoveQuestionFromQuizCommand;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;

public class RemoveQuestionFromQuizCommandHandler implements Command.Handler<RemoveQuestionFromQuizCommand, Void> {

    private CourseRepository courseRepository;

    public RemoveQuestionFromQuizCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Void handle(RemoveQuestionFromQuizCommand removeQuestionFromQuizCommand) {
        var course = courseRepository.findByCourseId(removeQuestionFromQuizCommand.getIdsCourse().getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.removeQuestionFromQuiz(
                removeQuestionFromQuizCommand.getIdsCourse().getModuleId(),
                removeQuestionFromQuizCommand.getIdsCourse().getLessonId(),
                removeQuestionFromQuizCommand.getIdsCourse().getQuestionId());
        return null;
    }
}
