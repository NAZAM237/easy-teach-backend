package fr.cleanarchitecture.easyteach.course.application.usecases;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.GetCourseViewModel;

public class GetCourseByIdCommandHandler implements Command.Handler<GetCourseByIdCommand, GetCourseViewModel> {

    private final CourseRepository courseRepository;

    public GetCourseByIdCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public GetCourseViewModel handle(GetCourseByIdCommand getCourseByIdCommand) {
        var existingCourse = courseRepository.findByCourseId(getCourseByIdCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        return new GetCourseViewModel(existingCourse);
    }
}
