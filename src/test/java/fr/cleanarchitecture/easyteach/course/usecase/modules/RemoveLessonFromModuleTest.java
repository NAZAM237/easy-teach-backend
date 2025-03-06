package fr.cleanarchitecture.easyteach.course.usecase.modules;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.RemoveLessonFromModuleCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.RemoveLessonFromModuleCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryModuleRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RemoveLessonFromModuleTest {

    private final ModuleRepository moduleRepository = new InMemoryModuleRepository();
    private Module module;
    private Lesson lesson;

    @Before
    public void setUp() throws Exception {
        module = new Module("Introduction à JAVA", "Description", 1);
        lesson = new Lesson("Introduction", LessonType.TEXT, null, null, 1);
        module.addLesson(lesson);
        moduleRepository.save(module);
    }

    @Test
    public void removeLessonFromModuleTest() {
        var removeLessonFromModuleCommand = new RemoveLessonFromModuleCommand(module.getModuleId(), lesson.getLessonId());
        var removeLessonFromModuleCommandHandler = new RemoveLessonFromModuleCommandHandler(moduleRepository);

        removeLessonFromModuleCommandHandler.handle(removeLessonFromModuleCommand);

        Assert.assertTrue(module.getLessons().isEmpty());
    }

    @Test
    public void removeNotExistLessonFromModuleTest() {
        module.removeLesson(lesson.getLessonId());
        var removeLessonFromModuleCommand = new RemoveLessonFromModuleCommand(module.getModuleId(), lesson.getLessonId());
        var removeLessonFromModuleCommandHandler = new RemoveLessonFromModuleCommandHandler(moduleRepository);

        Assert.assertThrows(
                "Lesson not found",
                NotFoundException.class,
                () -> removeLessonFromModuleCommandHandler.handle(removeLessonFromModuleCommand)
        );
    }
}
