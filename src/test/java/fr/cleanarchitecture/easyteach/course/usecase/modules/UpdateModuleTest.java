package fr.cleanarchitecture.easyteach.course.usecase.modules;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.UpdateModuleCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.UpdateModuleCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryModuleRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UpdateModuleTest {

    private final ModuleRepository moduleRepository = new InMemoryModuleRepository();
    private Module module;

    @Before
    public void setUp() throws Exception {
        module = new Module("Introduction à la programmation Java", "Une description", 1);
    }

    @Test
    public void updateModuleTest() {
        moduleRepository.save(module);

        var updateModuleCommand = new UpdateModuleCommand(module.getModuleId(), "Introduction", "Desc");
        var updateModuleCommandHandler = new UpdateModuleCommandHandler(moduleRepository);

        updateModuleCommandHandler.handle(updateModuleCommand);

        var updatedModule = moduleRepository.findByModuleId(module.getModuleId());

        Assert.assertTrue(updatedModule.isPresent());
        Assert.assertEquals(updateModuleCommand.getModuleTitle(), updatedModule.get().getModuleTitle());
        Assert.assertEquals(updateModuleCommand.getModuleDescription(), updatedModule.get().getModuleDescription());
    }

    @Test
    public void updateNotFoundModuleTest_shouldThrowException() {
        var updateModuleCommand = new UpdateModuleCommand(module.getModuleId(), "Introduction", "Desc");
        var updateModuleCommandHandler = new UpdateModuleCommandHandler(moduleRepository);
        Assert.assertThrows(
                "Module not found",
                NotFoundException.class,
                () -> updateModuleCommandHandler.handle(updateModuleCommand)
        );
    }
}
