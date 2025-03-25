package fr.cleanarchitecture.easyteach.course.usecase;

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
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class UpdateLessonFromModuleTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private Course course;
    private Module module;
    private Lesson lesson;
    private InputLesson inputLesson;

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

        inputLesson = new InputLesson(
                "lessonTitle2",
                "DOCUMENTS",
                "Documents",
                null
        );
    }

    @Test
    public void updateLessonFromModuleTest() {
        var updateLessonFromModuleCommand = new UpdateLessonFromModuleCommand(
                course.getCourseId(), module.getModuleId(), lesson.getLessonId(), inputLesson
        );
        var updateLessonFromModuleCommandHandler = new UpdateLessonFromModuleCommandHandler(courseRepository);
        var lessonView = updateLessonFromModuleCommandHandler.handle(updateLessonFromModuleCommand);

        Assert.assertEquals(inputLesson.getTitle(), lessonView.getData().getLessonTitle());
        Assert.assertEquals(inputLesson.getContentFileUrl(), lessonView.getData().getContentFileUrl());
        Assert.assertEquals(ResourceType.DOCUMENTS, lessonView.getData().getContentType());
    }

    @Test
    public void updateLessonFromNotExistingModuleTest_shouldThrowException() {
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
