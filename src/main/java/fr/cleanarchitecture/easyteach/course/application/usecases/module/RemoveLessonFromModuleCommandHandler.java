package fr.cleanarchitecture.easyteach.course.application.usecases.module;

import an.awesome.pipelinr.Command;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;

public class RemoveLessonFromModuleCommandHandler implements Command.Handler<RemoveLessonFromModuleCommand, Void> {

    private final ModuleRepository moduleRepository;

    public RemoveLessonFromModuleCommandHandler(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    public Void handle(RemoveLessonFromModuleCommand removeLessonFromModuleCommand) {
        var module = moduleRepository.findByModuleId(removeLessonFromModuleCommand.getModuleId())
                .orElseThrow(() -> new NotFoundException("Module not found"));
        module.removeLesson(removeLessonFromModuleCommand.getLessonId());
        return null;
    }
}
