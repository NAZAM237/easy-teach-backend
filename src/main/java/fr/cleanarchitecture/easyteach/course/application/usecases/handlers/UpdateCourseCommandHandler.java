package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class UpdateCourseCommandHandler implements Command.Handler<UpdateCourseCommand, CourseViewModel> {

    private final CourseRepository courseRepository;

    public UpdateCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseViewModel handle(UpdateCourseCommand updateCourseCommand) {
        var existingCourse = courseRepository.findByCourseId(updateCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        existingCourse.updateCourseData(
                updateCourseCommand.getCourseTitle(),
                updateCourseCommand.getCourseDescription(),
                updateCourseCommand.getPrice()
        );

        this.courseRepository.save(existingCourse);
        return new CourseViewModel(
                "Your course was successfully updated",
                existingCourse
        );
    }
}
