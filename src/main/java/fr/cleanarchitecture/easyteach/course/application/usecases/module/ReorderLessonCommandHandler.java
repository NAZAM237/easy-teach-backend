package fr.cleanarchitecture.easyteach.course.application.usecases.module;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleViewModel;

public class ReorderLessonCommandHandler implements Command.Handler<ReorderLessonCommand, ModuleViewModel> {

    private final ModuleRepository moduleRepository;

    public ReorderLessonCommandHandler(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    public ModuleViewModel handle(ReorderLessonCommand reorderLessonCommand) {
        var module = moduleRepository.findByModuleId(reorderLessonCommand.getModuleId())
                .orElseThrow(() -> new NotFoundException("Module not found"));
        module.reorderLessons(reorderLessonCommand.getLessons());
        return new ModuleViewModel(module);
    }
}
