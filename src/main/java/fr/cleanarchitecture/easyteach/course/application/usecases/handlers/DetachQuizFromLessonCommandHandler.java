package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.DetachQuizFromLessonCommand;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;

public class DetachQuizFromLessonCommandHandler implements Command.Handler<DetachQuizFromLessonCommand, Void> {

    private CourseRepository courseRepository;

    public DetachQuizFromLessonCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Void handle(DetachQuizFromLessonCommand detachQuizFromLessonCommand) {
        var course = courseRepository.findByCourseId(detachQuizFromLessonCommand.getIdsCourse().getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.detachQuizFromLesson(
                detachQuizFromLessonCommand.getIdsCourse().getModuleId(),
                detachQuizFromLessonCommand.getIdsCourse().getLessonId());
        return null;
    }
}
