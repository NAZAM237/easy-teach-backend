package fr.cleanarchitecture.easyteach.course.application.usecases;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.authentication.application.ports.UserRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class UpdateCourseCommandHandler implements Command.Handler<UpdateCourseCommand, CourseViewModel> {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public UpdateCourseCommandHandler(UserRepository userRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseViewModel handle(UpdateCourseCommand updateCourseCommand) {
        var existingCourse = courseRepository.findByCourseId(updateCourseCommand.getCourseId());
        if (existingCourse.isEmpty()) {
            throw new IllegalStateException("Course does not exist");
        }
        existingCourse.get().changeTitle(updateCourseCommand.getCourseTitle());
        existingCourse.get().changeDescription(updateCourseCommand.getCourseDescription());
        var course = existingCourse.get().changePrice(updateCourseCommand.getPrice());
        return new CourseViewModel(
                "Your course was successfully updated",
                course
        );
    }
}
