package fr.cleanarchitecture.easyteach.course.application.usecases.course;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.CourseStatus;

public class DeleteCourseCommandHandler implements Command.Handler<DeleteCourseCommand, Void> {

    private final CourseRepository courseRepository;

    public DeleteCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Void handle(DeleteCourseCommand deleteCourseCommand) {
        var existingCourse = courseRepository.findByCourseId(deleteCourseCommand.getCourseId());

        if (existingCourse.isEmpty()) {
            throw new NotFoundException("Course not found");
        }

        if (existingCourse.get().getStatus().equals(CourseStatus.PUBLISHED)) {
            throw new BadRequestException("Unable to delete. Course must be draft or archived to be deleted");
        }

        courseRepository.delete(existingCourse.get());
        return null;
    }
}
