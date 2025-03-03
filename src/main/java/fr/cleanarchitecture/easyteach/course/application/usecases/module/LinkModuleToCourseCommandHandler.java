package fr.cleanarchitecture.easyteach.course.application.usecases.module;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleViewModel;

public class LinkModuleToCourseCommandHandler implements Command.Handler<LinkModuleToCourseCommand, ModuleViewModel> {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    public LinkModuleToCourseCommandHandler(CourseRepository courseRepository, ModuleRepository moduleRepository) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public ModuleViewModel handle(LinkModuleToCourseCommand linkModuleToCourseCommand) {
        var course = courseRepository.findByCourseId(linkModuleToCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        var module = moduleRepository.findByModuleId(linkModuleToCourseCommand.getModuleId())
                .orElseThrow(() -> new NotFoundException("Module not found"));
        course.addModule(module);
        courseRepository.save(course);
        return new ModuleViewModel(module);
    }
}
