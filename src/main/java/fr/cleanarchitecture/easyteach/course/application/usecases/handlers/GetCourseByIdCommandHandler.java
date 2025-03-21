package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.GetCourseByIdCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;

public class GetCourseByIdCommandHandler implements Command.Handler<GetCourseByIdCommand, BaseViewModel<Course>> {

    private final CourseRepository courseRepository;

    public GetCourseByIdCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Course> handle(GetCourseByIdCommand getCourseByIdCommand) {
        var existingCourse = courseRepository.findByCourseId(getCourseByIdCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        return new BaseViewModel<>(existingCourse);
    }
}
