package fr.cleanarchitecture.easyteach.course.application.usecases.module;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;

public class RemoveLessonFromModuleCommandHandler implements Command.Handler<RemoveLessonFromModuleCommand, Void> {

    private final CourseRepository courseRepository;

    public RemoveLessonFromModuleCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Void handle(RemoveLessonFromModuleCommand removeLessonFromModuleCommand) {
        var course = courseRepository.findByCourseId(removeLessonFromModuleCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        course.removeLessonToModule(
                removeLessonFromModuleCommand.getModuleId(),
                removeLessonFromModuleCommand.getLessonId());
        return null;
    }
}
