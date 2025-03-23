package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AttachQuizToLessonCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.AttachQuizToLessonCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.*;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class AttachQuizToLessonTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();

    private Course course;
    private Lesson lesson;
    private Module module;
    private Quiz quiz;
    private IdsCourse idsCourse;

    @Before
    public void setUp() {
        course = new Course("Apprenez à programmer en Java", "Description...", new Teacher(), new Price(BigDecimal.ZERO, "FCFA"));
        module = new Module("Introduction à Java", "Description module...", 1);
        lesson = new Lesson("Introduction", ResourceType.DOCUMENTS, null, "TextContent", 1);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
        courseRepository.save(course);
    }

    @Before
    public void setupQuestion() {
        idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId());
        quiz = new Quiz(
                "Quiz sur le POO",
                "Ce quiz teste vos connaissances de base sur la POO",
                70);
    }

    @Test
    public void attachQuizToLessonTest() {
        var attachQuizToLessonCommand = new AttachQuizToLessonCommand(idsCourse, quiz);
        var attachQuizToLessonCommandHandler = new AttachQuizToLessonCommandHandler(courseRepository);

        attachQuizToLessonCommandHandler.handle(attachQuizToLessonCommand);

        Assert.assertEquals(quiz.getQuizTitle(), lesson.getQuiz().getQuizTitle());
        Assert.assertEquals(quiz.getDescription(), lesson.getQuiz().getDescription());
        Assert.assertEquals(quiz.getPassingScore(), lesson.getQuiz().getPassingScore());
    }

    @Test
    public void attachQuizToLessonWithAlreadyQuizTest_shouldThrowException() {
        var attachQuizToLessonCommand = new AttachQuizToLessonCommand(idsCourse, quiz);
        var attachQuizToLessonCommandHandler = new AttachQuizToLessonCommandHandler(courseRepository);

        attachQuizToLessonCommandHandler.handle(attachQuizToLessonCommand);

         var assertResult = Assert.assertThrows(
                BadRequestException.class,
                () -> attachQuizToLessonCommandHandler.handle(attachQuizToLessonCommand)
        );
        Assert.assertEquals("Quiz is already attached", assertResult.getMessage());
    }
}
