package fr.cleanarchitecture.easyteach.course.domain;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ModuleTest {

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
    }

    @Test
    public void reOrderLessonsTest() {
        List<Lesson> newOrder = Arrays.asList(lesson3, lesson1, lesson2);
        module.reorderLessons(newOrder);

        Assert.assertEquals(lesson3.getOrder(), 1);
        Assert.assertEquals(lesson1.getOrder(), 2);
        Assert.assertEquals(lesson2.getOrder(), 3);
    }

    @Test
    public void reOrderLessonIfLessonNotInModule_shouldThrowException() {
        Lesson lesson4 = new Lesson("Exceptions", LessonType.TEXT, null, "Gestion des erreurs", 4);
        List<Lesson> invalidOrder = Arrays.asList(lesson3, lesson1, lesson4);

        Assert.assertThrows(
                "Invalid lessons list",
                BadRequestException.class,
                () -> module.reorderLessons(invalidOrder)
        );
    }

    @Test
    public void reOrderLessonIfLessonsListIsIncompleteTest_shouldThrowException() {
        List<Lesson> incompleteOrder = Arrays.asList(lesson1, lesson2);

        Assert.assertThrows(
                "Invalid lessons list",
                BadRequestException.class,
                () -> module.reorderLessons(incompleteOrder)
        );
    }

    @Test
    public void addLessonToModuleTest() {
        var lesson = new Lesson("title", LessonType.TEXT, "videoUrl", "textContent", 1);
        var module = new Module("title", "description", 1);
        module.addLesson(lesson);
        Assert.assertFalse(module.getLessons().isEmpty());
        Assert.assertEquals(1, module.getLessons().size());
    }

    @Test
    public void removeLessonFromModuleTest() {
        var lesson = new Lesson("title", LessonType.TEXT, "videoUrl", "textContent", 1);
        var module = new Module("title", "description", 1);
        module.addLesson(lesson);
        Assert.assertFalse(module.getLessons().isEmpty());
        module.removeLesson(lesson.getLessonId());
        Assert.assertTrue(module.getLessons().isEmpty());
    }

    @Test
    public void addLessonToModuleWhenLessonPositionIsAlreadyInUse_shouldFailTest() {
        var lesson = new Lesson("title", LessonType.TEXT, "videoUrl", "textContent", 1);
        var module = new Module("title", "description", 1);
        module.addLesson(lesson);

        Assert.assertThrows(
            "The lesson position already in use",
             BadRequestException.class,
            () -> module.addLesson(lesson)
        );
    }

    @Test
    public void removeNotExistingLessonFromModule_shouldFailTest() {
        var module = new Module("title", "description", 1);
        Assert.assertThrows(
                "The lesson not found",
                NotFoundException.class,
                (() -> module.removeLesson("garbage")));
    }
}
