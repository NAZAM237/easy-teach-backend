package fr.cleanarchitecture.easyteach.course.application.usecases.handlers;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.core.domain.viewmodel.BaseViewModel;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AddModuleToCourseCommand;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;

public class AddModuleToCourseCommandHandler implements Command.Handler<AddModuleToCourseCommand, BaseViewModel<Module>> {

    private final CourseRepository courseRepository;

    public AddModuleToCourseCommandHandler(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public BaseViewModel<Module> handle(AddModuleToCourseCommand addModuleToCourseCommand) {
        var course = courseRepository.findByCourseId(addModuleToCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        var module = new Module(
                addModuleToCourseCommand.getModuleTitle(),
                addModuleToCourseCommand.getModuleDescription(),
                addModuleToCourseCommand.getModuleOrder()
        );
        course.addModule(module);
        courseRepository.save(course);
        return new BaseViewModel<>(module);
    }
}
