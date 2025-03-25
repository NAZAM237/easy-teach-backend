package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.RemoveModuleFromCourseCommand;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;

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
            throw new BadRequestException("Module not found in course");
        }

        existingCourse.removeModule(removeModuleFromCourseCommand.getModuleId());
        courseRepository.save(existingCourse);
        return null;
    }
}
