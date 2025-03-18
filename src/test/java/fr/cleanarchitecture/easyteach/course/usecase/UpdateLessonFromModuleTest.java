package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateLessonFromModuleCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.UpdateLessonFromModuleCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.InputLesson;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class UpdateLessonFromModuleTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private Course course;
    private Module module;
    private Lesson lesson;

    @Before
    public void setUp() {
        course = new Course(
                "courseTitle",
                "courseDescription",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        module = new Module("moduleTitle", "moduleDescription", 1);
        lesson = new Lesson("lessonTitle", ResourceType.IMAGES, null, "textContent", 1);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
        courseRepository.save(course);
    }

    @Test
    public void updateLessonFromModuleTest() {
        var inputLesson = new InputLesson(
                "lessonTitle2",
                "DOCUMENTS",
                "Documents",
                null
        );
        var updateLessonFromModuleCommand = new UpdateLessonFromModuleCommand(
                course.getCourseId(), module.getModuleId(), lesson.getLessonId(), inputLesson
        );
        var updateLessonFromModuleCommandHandler = new UpdateLessonFromModuleCommandHandler(courseRepository);
        var courseView = updateLessonFromModuleCommandHandler.handle(updateLessonFromModuleCommand);

        var updatedModule = courseView.getCourse().getModules()
                .stream()
                .filter(module1 -> module1.getModuleId().equals(module.getModuleId()))
                .findFirst()
                .orElseThrow();
        var updatedLesson = updatedModule.getLessons()
                .stream()
                .filter(lesson1 -> lesson1.getLessonId().equals(lesson.getLessonId()))
                .findFirst()
                .orElseThrow();

        Assert.assertEquals("lessonTitle2", updatedLesson.getLessonTitle());
        Assert.assertEquals(ResourceType.DOCUMENTS, updatedLesson.getContentType());
    }

    @Test
    public void updateLessonFromNotExistingModuleTest_shouldThrowException() {
        var inputLesson = new InputLesson(
                "lessonTitle2",
                "DOCUMENTS",
                null,
                null
        );
        var updateLessonFromModuleCommand = new UpdateLessonFromModuleCommand(
                course.getCourseId(), "Garbage", lesson.getLessonId(), inputLesson
        );
        var updateLessonFromModuleCommandHandler = new UpdateLessonFromModuleCommandHandler(courseRepository);

        Assert.assertThrows(
                "Module not found",
                NotFoundException.class,
                () -> updateLessonFromModuleCommandHandler.handle(updateLessonFromModuleCommand)
        );
    }

    @Test
    public void updateNotExistLessonFromModuleTest_shouldThrowException() {
        var inputLesson = new InputLesson(
                "lessonTitle2",
                "DOCUMENTS",
                null,
                null
        );
        var updateLessonFromModuleCommand = new UpdateLessonFromModuleCommand(
                course.getCourseId(), module.getModuleId(), "Garbage", inputLesson
        );
        var updateLessonFromModuleCommandHandler = new UpdateLessonFromModuleCommandHandler(courseRepository);

        Assert.assertThrows(
                "Lesson not found",
                NotFoundException.class,
                () -> updateLessonFromModuleCommandHandler.handle(updateLessonFromModuleCommand)
        );
    }
}
