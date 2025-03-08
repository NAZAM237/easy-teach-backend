package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateLessonFromModuleCommand;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;

public class UpdateLessonFromModuleCommandHandler implements Command.Handler<UpdateLessonFromModuleCommand, CourseViewModel> {
    private final CourseRepository courseRepository;

    public UpdateLessonFromModuleCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseViewModel handle(UpdateLessonFromModuleCommand updateLessonFromModuleCommand) {
        var course = courseRepository.findByCourseId(updateLessonFromModuleCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        var module = course.getModules()
                .stream()
                .filter(module1 -> module1.getModuleId().equals(updateLessonFromModuleCommand.getModuleId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Module not found"));
        module.updateLessonData(
                updateLessonFromModuleCommand.getLessonId(),
                updateLessonFromModuleCommand.getLesson());
        courseRepository.save(course);

        return new CourseViewModel(
                "Lesson " + updateLessonFromModuleCommand.getLessonId() + "updated successfully",
                course);
    }
}
