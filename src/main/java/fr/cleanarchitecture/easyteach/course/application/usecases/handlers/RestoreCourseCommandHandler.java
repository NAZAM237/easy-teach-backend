package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.RestoreCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;

public class RestoreCourseCommandHandler implements Command.Handler<RestoreCourseCommand, BaseViewModel<Course>> {

    private final CourseRepository courseRepository;

    public RestoreCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Course> handle(RestoreCourseCommand restoreCourseCommand) {
        var existingCourse = courseRepository.findByCourseId(restoreCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        var restoredCourse = existingCourse.restore();
        courseRepository.save(restoredCourse);
        return new BaseViewModel<>(restoredCourse);
    }
}
