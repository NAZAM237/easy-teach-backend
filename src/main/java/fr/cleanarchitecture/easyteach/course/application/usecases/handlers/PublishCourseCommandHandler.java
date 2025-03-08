package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.PublishCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class PublishCourseCommandHandler implements Command.Handler<PublishCourseCommand, CourseViewModel> {

    private final CourseRepository courseRepository;

    public PublishCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseViewModel handle(PublishCourseCommand publishCourseCommand) {
        var existingCourse = courseRepository.findByCourseId(publishCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("course not found"));
        existingCourse.publish();
        courseRepository.save(existingCourse);
        return new CourseViewModel("course published successfully", existingCourse);
    }
}
