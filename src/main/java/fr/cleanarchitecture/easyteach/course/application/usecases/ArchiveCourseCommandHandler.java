package fr.cleanarchitecture.easyteach.course.application.usecases;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class ArchiveCourseCommandHandler implements Command.Handler<ArchiveCourseCommand, CourseViewModel> {

    private final CourseRepository courseRepository;

    public ArchiveCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseViewModel handle(ArchiveCourseCommand archiveCourseCommand) {
        var existingCourse = courseRepository.findByCourseId(archiveCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        var archivedCourse = existingCourse.archive();
        courseRepository.save(archivedCourse);

        return new CourseViewModel("Course archived successfully", archivedCourse);
    }
}
