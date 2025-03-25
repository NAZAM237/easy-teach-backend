package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateModuleFromCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.shared.domain.viewmodel.BaseViewModel;

public class UpdateModuleFromCourseCommandHandler implements Command.Handler<UpdateModuleFromCourseCommand, BaseViewModel<Module>> {

    private CourseRepository courseRepository;

    public UpdateModuleFromCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Module> handle(UpdateModuleFromCourseCommand updateModuleFromCourseCommand) {
        var course = courseRepository.findByCourseId(updateModuleFromCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        course.updateModuleData(
                updateModuleFromCourseCommand.getModuleId(),
                updateModuleFromCourseCommand.getModuleTitle(),
                updateModuleFromCourseCommand.getModuleDescription()
        );
        courseRepository.save(course);
        var module = course.getModules()
                .stream()
                .filter(module1 -> module1.getModuleId().equals(updateModuleFromCourseCommand.getModuleId()))
                .findFirst().orElseThrow();
        return new BaseViewModel<>(module);
    }
}
