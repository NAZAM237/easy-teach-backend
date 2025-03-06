package fr.cleanarchitecture.easyteach.course.usecase.modules;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.DeleteModuleCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.DeleteModuleCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryModuleRepository;
import org.junit.Assert;
import org.junit.Test;

public class DeleteModuleTest {

    private final ModuleRepository moduleRepository = new InMemoryModuleRepository();

    @Test
    public void deleteModuleTest() {
        var module = new Module("Introduction", "Description", 1);
        moduleRepository.save(module);

        var deleteModuleCommand = new DeleteModuleCommand(module.getModuleId());
        var deleteModuleCommandHandler = new DeleteModuleCommandHandler(moduleRepository);

        deleteModuleCommandHandler.handle(deleteModuleCommand);

        var deletedModule = moduleRepository.findByModuleId(module.getModuleId());
        Assert.assertFalse(deletedModule.isPresent());
    }

    @Test
    public void deleteNotExistingModuleTest_throwsException() {
        var module = new Module("Introduction", "Description", 1);

        var deleteModuleCommand = new DeleteModuleCommand(module.getModuleId());
        var deleteModuleCommandHandler = new DeleteModuleCommandHandler(moduleRepository);

        Assert.assertThrows(
                "Module not found",
                NotFoundException.class,
                () -> deleteModuleCommandHandler.handle(deleteModuleCommand)
        );
    }
}
