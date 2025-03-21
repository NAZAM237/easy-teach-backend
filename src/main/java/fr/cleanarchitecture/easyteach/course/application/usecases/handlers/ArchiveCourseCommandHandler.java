package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.ArchiveCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;

public class ArchiveCourseCommandHandler implements Command.Handler<ArchiveCourseCommand, BaseViewModel<Course>> {

    private final CourseRepository courseRepository;

    public ArchiveCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Course> handle(ArchiveCourseCommand archiveCourseCommand) {
        var existingCourse = courseRepository.findByCourseId(archiveCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        var archivedCourse = existingCourse.archive();
        courseRepository.save(archivedCourse);

        return new BaseViewModel<>(archivedCourse);
    }
}
