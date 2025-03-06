package fr.cleanarchitecture.easyteach.course.usecase.modules;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.AddLessonToModuleCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.AddLessonToModuleCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.InputLesson;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryModuleRepository;
import org.junit.Assert;
import org.junit.Test;

public class AddLessonToModuleTests {

    private final ModuleRepository moduleRepository = new InMemoryModuleRepository();

    @Test
    public void addLessonToModuleTest() {
        var module = new Module("Introduction à JAVA", "Cours complet", 1);
        moduleRepository.save(module);

        var inputLesson = new InputLesson("title", LessonType.TEXT, "videoUrl", "textContent", 1);
        var addLessonToModuleCommand = new AddLessonToModuleCommand(
                module.getModuleId(), inputLesson);
        var addLessonToModuleCommandHandler = new AddLessonToModuleCommandHandler(moduleRepository);

        var result = addLessonToModuleCommandHandler.handle(addLessonToModuleCommand);

        Assert.assertEquals(result.getModuleId(), module.getModuleId());
        Assert.assertEquals(1, result.getLessons().size());
    }

    @Test
    public void addLessonToNotExistModuleTest() {
        var module = new Module("Introduction à JAVA", "Cours complet", 1);

        var inputLesson = new InputLesson("title", LessonType.TEXT, "videoUrl", "textContent", 1);
        var addLessonToModuleCommand = new AddLessonToModuleCommand(
                module.getModuleId(), inputLesson);
        var addLessonToModuleCommandHandler = new AddLessonToModuleCommandHandler(moduleRepository);

        Assert.assertThrows(
                "Module not found",
                NotFoundException.class,
                () -> addLessonToModuleCommandHandler.handle(addLessonToModuleCommand)
        );
    }

    @Test
    public void addLessonToModuleWhenPositionAlreaydInUseTest_shouldThrowException() {
        var module = new Module("Introduction à JAVA", "Cours complet", 1);
        var lesson = new Lesson("title", LessonType.TEXT, "videoUrl", "textContent", 1);
        module.addLesson(lesson);
        moduleRepository.save(module);
        var inputLesson = new InputLesson("title", LessonType.TEXT, "videoUrl", "textContent", 1);
        var addLessonToModuleCommand = new AddLessonToModuleCommand(
                module.getModuleId(), inputLesson);
        var addLessonToModuleCommandHandler = new AddLessonToModuleCommandHandler(moduleRepository);

        Assert.assertThrows(
                "Lesson position already in use",
                BadRequestException.class,
                () -> addLessonToModuleCommandHandler.handle(addLessonToModuleCommand)
        );
    }
}
