package fr.cleanarchitecture.easyteach.course.application.usecases.module;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleViewModel;

public class UnLinkModuleToCourseCommandHandler implements Command.Handler<UnLinkModuleToCourseCommand, ModuleViewModel> {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    public UnLinkModuleToCourseCommandHandler(CourseRepository courseRepository, ModuleRepository moduleRepository) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public ModuleViewModel handle(UnLinkModuleToCourseCommand unLinkModuleToCourseCommand) {
        var course = courseRepository.findByCourseId(unLinkModuleToCourseCommand.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        var module = course.getModules()
                .stream()
                .filter(module1 -> module1.getModuleId().equals(unLinkModuleToCourseCommand.getModuleId()))
                .findFirst();

        if (module.isEmpty()) {
            throw new BadRequestException("Module is not linked to course");
        }
        course.removeModule(module.get().getModuleId());
        return new ModuleViewModel(module.get());
    }
}
