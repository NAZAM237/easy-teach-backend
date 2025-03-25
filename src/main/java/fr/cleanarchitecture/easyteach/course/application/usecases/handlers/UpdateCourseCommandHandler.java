package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

public class UpdateCourseCommandHandler implements Command.Handler<UpdateCourseCommand, BaseViewModel<Course>> {

    private final CourseRepository courseRepository;

    public UpdateCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Course> handle(UpdateCourseCommand updateCourseCommand) {
        var existingCourse = courseRepository.findByCourseId(updateCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        existingCourse.updateCourseData(
                updateCourseCommand.getCourseTitle(),
                updateCourseCommand.getCourseDescription(),
                updateCourseCommand.getPrice()
        );

        this.courseRepository.save(existingCourse);
        return new BaseViewModel<>(existingCourse);
    }
}
