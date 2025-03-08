package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.DeleteCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.enums.CourseStatus;

public class DeleteCourseCommandHandler implements Command.Handler<DeleteCourseCommand, Void> {

    private final CourseRepository courseRepository;

    public DeleteCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Void handle(DeleteCourseCommand deleteCourseCommand) {
        var existingCourse = courseRepository.findByCourseId(deleteCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        if (existingCourse.getStatus().equals(CourseStatus.PUBLISHED)) {
            throw new BadRequestException("Unable to delete. Course must be draft or archived to be deleted");
        }

        courseRepository.delete(existingCourse);
        return null;
    }
}
