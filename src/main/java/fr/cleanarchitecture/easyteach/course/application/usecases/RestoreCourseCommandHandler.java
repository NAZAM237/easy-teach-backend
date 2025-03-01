package fr.cleanarchitecture.easyteach.course.application.usecases;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class RestoreCourseCommandHandler implements Command.Handler<RestoreCourseCommand, CourseViewModel> {

    private final CourseRepository courseRepository;

    public RestoreCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseViewModel handle(RestoreCourseCommand restoreCourseCommand) {
        var existingCourse = courseRepository.findByCourseId(restoreCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        var restoredCourse = existingCourse.restore();
        courseRepository.save(restoredCourse);
        return new CourseViewModel(
                "Course restored successfully",
                restoredCourse);
    }
}
