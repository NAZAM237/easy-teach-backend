package fr.cleanarchitecture.easyteach.course.usecase.modules;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.ReorderLessonCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.ReorderLessonCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryModuleRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ReorderLessonInModuleTests {

    private final ModuleRepository moduleRepository = new InMemoryModuleRepository();

    private Module module;
    private Lesson lesson1;
    private Lesson lesson2;
    private Lesson lesson3;

    @Before
    public void setUp() {
        module = new Module("Programmation Java", "Description", 1);

        lesson1 = new Lesson("Introduction", LessonType.TEXT, null, "Intro", 1);
        lesson2 = new Lesson("Variables", LessonType.TEXT, null, "Les types", 2);
        lesson3 = new Lesson("Boucles", LessonType.TEXT, null, "Les boucles", 3);

        module.addLesson(lesson1);
        module.addLesson(lesson2);
        module.addLesson(lesson3);
        moduleRepository.save(module);
    }

    @Test
    public void reorderLessonTest() {
        var lessons = List.of(lesson3, lesson1, lesson2);
        var reorderLessonCommand = new ReorderLessonCommand(module.getModuleId(), lessons);
        var reorderLessonCommandHandler = new ReorderLessonCommandHandler(moduleRepository);

        reorderLessonCommandHandler.handle(reorderLessonCommand);

        Assert.assertEquals(1, lesson3.getOrder());
        Assert.assertEquals(2, lesson1.getOrder());
        Assert.assertEquals(3, lesson2.getOrder());
    }

    @Test
    public void reorderLessonInNotFoundModuleTest_shouldThrowException() {
        var lessons = List.of(lesson3, lesson1, lesson2);
        var reorderLessonCommand = new ReorderLessonCommand("Garbage", lessons);
        var reorderLessonCommandHandler = new ReorderLessonCommandHandler(moduleRepository);

        Assert.assertThrows(
                "Module not found",
                NotFoundException.class,
                () -> reorderLessonCommandHandler.handle(reorderLessonCommand)
        );
    }
}
