package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.DetachQuizFromLessonCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.DetachQuizFromLessonCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.*;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.BadRequestException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class DetachQuizFromLessonTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();

    private Course course;
    private Lesson lesson;
    private Module module;
    private Quiz quiz;
    private IdsCourse idsCourse;

    @Before
    public void setUp() {
        quiz = new Quiz(
                "Quiz sur le POO",
                "Ce quiz teste vos connaissances de base sur la POO",
                70);
        course = new Course("Apprenez à programmer en Java", "Description...", new Teacher(), new Price(BigDecimal.ZERO, "FCFA"));
        module = new Module("Introduction à Java", "Description module...", 1);
        lesson = new Lesson("Introduction", ResourceType.DOCUMENTS, null, "TextContent", 1);
        idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId());
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
        courseRepository.save(course);
    }

    @Test
    public void detachQuizFromLessonTest() {
        course.attachQuizToLesson(module.getModuleId(), lesson.getLessonId(), quiz);
        var detachQuizFromLessonCommand = new DetachQuizFromLessonCommand(idsCourse);
        var detachQuizFromLessonCommandHandler = new DetachQuizFromLessonCommandHandler(courseRepository);

        detachQuizFromLessonCommandHandler.handle(detachQuizFromLessonCommand);

        Assert.assertNull(lesson.getQuiz());
    }

    @Test
    public void detachUnAttachQuizFromLesson_shouldThrowException() {
        var detachQuizFromLessonCommand = new DetachQuizFromLessonCommand(idsCourse);
        var detachQuizFromLessonCommandHandler = new DetachQuizFromLessonCommandHandler(courseRepository);

        var resultThrow = Assert.assertThrows(
                BadRequestException.class,
                () -> detachQuizFromLessonCommandHandler.handle(detachQuizFromLessonCommand));
        Assert.assertEquals("Cannot detach quiz. This lesson hasn't attached quiz", resultThrow.getMessage());
    }
}
