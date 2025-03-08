package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.ReorderModuleInCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class ReorderModuleInCourseCommandHandler implements Command.Handler<ReorderModuleInCourseCommand, CourseViewModel> {

    private final CourseRepository courseRepository;

    public ReorderModuleInCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseViewModel handle(ReorderModuleInCourseCommand reorderModuleInCourseCommand) {
        var course = courseRepository.findByCourseId(reorderModuleInCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.reorderModules(reorderModuleInCourseCommand.getModules());
        courseRepository.save(course);
        return new CourseViewModel(
                "Module ordered successfully",
                course
        );
    }
}
