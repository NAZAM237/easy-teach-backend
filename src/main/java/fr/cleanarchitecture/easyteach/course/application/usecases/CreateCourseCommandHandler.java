package fr.cleanarchitecture.easyteach.course.application.usecases;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

import java.time.LocalDateTime;

public class CreateCourseCommandHandler implements Command.Handler<CreateCourseCommand, CourseViewModel> {

    private final CourseRepository courseRepository;

    public CreateCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseViewModel handle(CreateCourseCommand createCourseCommand) {
        var course = new Course(
                createCourseCommand.getCourseTitle(),
                createCourseCommand.getCourseDescription(),
                createCourseCommand.getInstructor(),
                createCourseCommand.getPrice()
        );

        this.courseRepository.save(course);

        return new CourseViewModel(
                course.getCourseId(),
                "Course created successfully",
                LocalDateTime.now()
        );
    }
}
