package fr.cleanarchitecture.easyteach.course.application.usecases.course;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.authentication.application.ports.UserRepository;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class CreateCourseCommandHandler implements Command.Handler<CreateCourseCommand, CourseViewModel> {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CreateCourseCommandHandler(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CourseViewModel handle(CreateCourseCommand createCourseCommand) {
        var existingCourse = this.courseRepository.findByTitle(createCourseCommand.getCourseTitle());
        var user = this.userRepository.findById(createCourseCommand.getTeacherUuid());

        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        if (existingCourse.isPresent()) {
            throw new BadRequestException("Course already exists");
        }

        var teacher = new Teacher(
                user.get().getUserId(),
                user.get().getUserName(),
                user.get().getUserBiography(),
                user.get().getUserEmail(),
                user.get().getUserPhone(),
                user.get().getUserPhoto());

        var course = new Course(
                createCourseCommand.getCourseTitle(),
                createCourseCommand.getCourseDescription(),
                teacher,
                createCourseCommand.getPrice()
        );

        this.courseRepository.save(course);

        return new CourseViewModel(
                "A new course was created successfully",
                course
        );
    }
}
