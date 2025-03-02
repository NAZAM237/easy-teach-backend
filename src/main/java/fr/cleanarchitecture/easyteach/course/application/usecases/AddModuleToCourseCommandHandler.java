package fr.cleanarchitecture.easyteach.course.application.usecases;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;

public class AddModuleToCourseCommandHandler implements Command.Handler<AddModuleToCourseCommand, Void> {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    public AddModuleToCourseCommandHandler(CourseRepository courseRepository, ModuleRepository moduleRepository) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public Void handle(AddModuleToCourseCommand addModuleToCourseCommand) {
        var course = courseRepository.findByCourseId(addModuleToCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        var module = new Module(
                addModuleToCourseCommand.getModuleTitle(),
                addModuleToCourseCommand.getModuleDescription(),
                addModuleToCourseCommand.getModuleOrder()
        );
        course.addModule(module);
        moduleRepository.save(module);
        courseRepository.save(course);
        return null;
    }
}
