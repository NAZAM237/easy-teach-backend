package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.ArchiveCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

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
