package fr.cleanarchitecture.easyteach.course.application.usecases.course;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;

public class RemoveModuleFromCourseCommandHandler implements Command.Handler<RemoveModuleFromCourseCommand, Void> {

    private final CourseRepository courseRepository;

    public RemoveModuleFromCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Void handle(RemoveModuleFromCourseCommand removeModuleFromCourseCommand) {
        var existingCourse = courseRepository.findByCourseId(removeModuleFromCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        var isModuleNotExist = existingCourse.getModules()
                .stream()
                .noneMatch(module -> module.getModuleId().equals(removeModuleFromCourseCommand.getModuleId()));

        if (isModuleNotExist) {
            throw new BadRequestException("Module not linked to course");
        }

        existingCourse.removeModule(removeModuleFromCourseCommand.getModuleId());
        return null;
    }
}
