package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.CreateCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

public class CreateCourseCommandHandler implements Command.Handler<CreateCourseCommand, BaseViewModel<Course>> {

    private final CourseRepository courseRepository;

    public CreateCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Course> handle(CreateCourseCommand createCourseCommand) {
        var existingCourse = this.courseRepository.findByTitle(createCourseCommand.getCourseTitle());

        if (existingCourse.isPresent()) {
            throw new BadRequestException("Course already exists");
        }

        var course = new Course(
                createCourseCommand.getCourseTitle(),
                createCourseCommand.getCourseDescription(),
                new Teacher(),
                createCourseCommand.getPrice()
        );

        this.courseRepository.save(course);

        return new BaseViewModel<>(course);
    }
}
