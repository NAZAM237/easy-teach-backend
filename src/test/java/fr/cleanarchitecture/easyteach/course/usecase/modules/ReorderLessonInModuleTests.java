package fr.cleanarchitecture.easyteach.course.usecase.modules;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.ReorderLessonFromModuleCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.ReorderLessonFromModuleCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class ReorderLessonInModuleTests {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private Course course;
    private Module module;
    private Lesson lesson1;
    private Lesson lesson2;
    private Lesson lesson3;

    @Before
    public void setUp() {
        course = new Course(
                "courseTitle",
                "courseDescription",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        module = new Module("Programmation Java", "Description", 1);
        lesson1 = new Lesson("Introduction", LessonType.TEXT, null, "Intro", 1);
        lesson2 = new Lesson("Variables", LessonType.TEXT, null, "Les types", 2);
        lesson3 = new Lesson("Boucles", LessonType.TEXT, null, "Les boucles", 3);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson1);
        course.addLessonToModule(module.getModuleId(), lesson2);
        course.addLessonToModule(module.getModuleId(), lesson3);
        courseRepository.save(course);
    }

    @Test
    public void reorderLessonTest() {
        var lessons = List.of(lesson3, lesson1, lesson2);
        var reorderLessonCommand = new ReorderLessonFromModuleCommand(
                course.getCourseId(),
                module.getModuleId(),
                lessons);
        var reorderLessonCommandHandler = new ReorderLessonFromModuleCommandHandler(courseRepository);

        reorderLessonCommandHandler.handle(reorderLessonCommand);

        Assert.assertEquals(1, lesson3.getOrder());
        Assert.assertEquals(2, lesson1.getOrder());
        Assert.assertEquals(3, lesson2.getOrder());
    }

    @Test
    public void reorderLessonInNotFoundModuleTest_shouldThrowException() {
        var lessons = List.of(lesson3, lesson1, lesson2);
        var reorderLessonCommand = new ReorderLessonFromModuleCommand(
                course.getCourseId(),
                "Garbage",
                lessons);
        var reorderLessonCommandHandler = new ReorderLessonFromModuleCommandHandler(courseRepository);

        Assert.assertThrows(
                "Module not found",
                NotFoundException.class,
                () -> reorderLessonCommandHandler.handle(reorderLessonCommand)
        );
    }
}
