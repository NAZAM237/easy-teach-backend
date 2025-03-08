package fr.cleanarchitecture.easyteach.course.usecase.modules;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.AddLessonToModuleCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.AddLessonToModuleCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.InputLesson;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class AddLessonToModuleTests {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();

    @Test
    public void addLessonToModuleTest() {
        var course = new Course(
                "courseTitle",
                "courseDescription",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        var module = new Module("Introduction à JAVA", "Cours complet", 1);
        course.addModule(module);
        courseRepository.save(course);

        var inputLesson = new InputLesson("title", LessonType.TEXT, "videoUrl", "textContent", 1);
        var addLessonToModuleCommand = new AddLessonToModuleCommand(
                course.getCourseId(),
                module.getModuleId(),
                inputLesson);
        var addLessonToModuleCommandHandler = new AddLessonToModuleCommandHandler(courseRepository);

        var result = addLessonToModuleCommandHandler.handle(addLessonToModuleCommand);

        Assert.assertEquals(1, result.getCourse().getModules().size());
    }

    @Test
    public void addLessonToNotExistModuleTest() {
        var course = new Course(
                "courseTitle",
                "courseDescription",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        var module = new Module("Introduction à JAVA", "Cours complet", 1);
        course.addModule(module);
        var inputLesson = new InputLesson("title", LessonType.TEXT, "videoUrl", "textContent", 1);
        var addLessonToModuleCommand = new AddLessonToModuleCommand(
                course.getCourseId(),
                "Garbage",
                inputLesson);
        var addLessonToModuleCommandHandler = new AddLessonToModuleCommandHandler(courseRepository);

        Assert.assertThrows(
                "Module not found",
                NotFoundException.class,
                () -> addLessonToModuleCommandHandler.handle(addLessonToModuleCommand)
        );
    }

    @Test
    public void addLessonToModuleWhenPositionAlreaydInUseTest_shouldThrowException() {
        var course = new Course(
                "courseTitle",
                "courseDescription",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        var module = new Module("Introduction à JAVA", "Cours complet", 1);
        var lesson = new Lesson("title", LessonType.TEXT, "videoUrl", "textContent", 1);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
        courseRepository.save(course);
        var inputLesson = new InputLesson("title", LessonType.TEXT, "videoUrl", "textContent", 1);
        var addLessonToModuleCommand = new AddLessonToModuleCommand(
                course.getCourseId(),
                module.getModuleId(),
                inputLesson);
        var addLessonToModuleCommandHandler = new AddLessonToModuleCommandHandler(courseRepository);

        Assert.assertThrows(
                "Lesson position already in use",
                BadRequestException.class,
                () -> addLessonToModuleCommandHandler.handle(addLessonToModuleCommand)
        );
    }
}
