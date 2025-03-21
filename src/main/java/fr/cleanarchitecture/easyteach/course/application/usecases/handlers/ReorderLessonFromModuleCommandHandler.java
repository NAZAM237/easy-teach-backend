package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.ReorderLessonFromModuleCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;

public class ReorderLessonFromModuleCommandHandler implements Command.Handler<ReorderLessonFromModuleCommand, BaseViewModel<Course>> {

    private final CourseRepository courseRepository;

    public ReorderLessonFromModuleCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Course> handle(ReorderLessonFromModuleCommand reorderLessonFromModuleCommand) {
        var course = courseRepository.findByCourseId(reorderLessonFromModuleCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        course.reorderLessonsFromModule(reorderLessonFromModuleCommand.getModuleId(), reorderLessonFromModuleCommand.getLessons());
        courseRepository.save(course);
        return new BaseViewModel<>(course);
    }
}
