package fr.cleanarchitecture.easyteach.course.application.usecases.module;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;

public class DeleteModuleCommandHandler implements Command.Handler<DeleteModuleCommand, Void> {

    private final ModuleRepository moduleRepository;

    public DeleteModuleCommandHandler(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    public Void handle(DeleteModuleCommand deleteModuleCommand) {
        var module = moduleRepository.findByModuleId(deleteModuleCommand.getModuleId())
                .orElseThrow(() -> new NotFoundException("Module not found"));
        moduleRepository.delete(module);
        return null;
    }
}
