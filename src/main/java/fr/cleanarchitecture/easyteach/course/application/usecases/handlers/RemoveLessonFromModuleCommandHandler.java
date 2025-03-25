package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.RemoveLessonFromModuleCommand;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;

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
