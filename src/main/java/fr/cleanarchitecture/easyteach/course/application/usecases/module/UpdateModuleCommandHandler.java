package fr.cleanarchitecture.easyteach.course.application.usecases.module;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleViewModel;

public class UpdateModuleCommandHandler implements Command.Handler<UpdateModuleCommand, ModuleViewModel> {

    private ModuleRepository moduleRepository;

    public UpdateModuleCommandHandler(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    public ModuleViewModel handle(UpdateModuleCommand updateModuleCommand) {
        var module = moduleRepository.findByModuleId(updateModuleCommand.getModuleId())
                .orElseThrow(() -> new NotFoundException("Module not found"));
        module.changeTitle(updateModuleCommand.getModuleTitle());
        module.changeDescription(updateModuleCommand.getModuleDescription());
        moduleRepository.save(module);
        return new ModuleViewModel(module);
    }
}
