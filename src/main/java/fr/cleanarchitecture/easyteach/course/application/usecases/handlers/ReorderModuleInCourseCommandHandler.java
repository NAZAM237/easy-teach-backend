package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.ReorderModuleInCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

public class ReorderModuleInCourseCommandHandler implements Command.Handler<ReorderModuleInCourseCommand, BaseViewModel<Course>> {

    private final CourseRepository courseRepository;

    public ReorderModuleInCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Course> handle(ReorderModuleInCourseCommand reorderModuleInCourseCommand) {
        var course = courseRepository.findByCourseId(reorderModuleInCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.reorderModules(reorderModuleInCourseCommand.getModules());
        courseRepository.save(course);
        return new BaseViewModel<>(course);
    }
}
