package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateQuizFromLessonCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.UpdateQuizFromLessonCommandHandler;
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

public class UpdateQuizFromLessonTest {

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
    public void updateQuizFromLessonTest() {
        course.attachQuizToLesson(module.getModuleId(), lesson.getLessonId(), quiz);
        var newQuiz = new Quiz("Quiz sur les concepts fondamentaux de la programmation java", "Description de ce quiz", 80);
        var updateQuizFromLessonCommand = new UpdateQuizFromLessonCommand(idsCourse, newQuiz);
        var updateQuizFromLessonCommandHandler = new UpdateQuizFromLessonCommandHandler(courseRepository);

        var result = updateQuizFromLessonCommandHandler.handle(updateQuizFromLessonCommand);

        Assert.assertEquals(newQuiz.getQuizTitle(), result.getData().getQuizTitle());
        Assert.assertEquals(newQuiz.getDescription(), result.getData().getDescription());
        Assert.assertEquals(newQuiz.getPassingScore(), result.getData().getPassingScore());
    }

    @Test
    public void updateUnAttachQuizFromLesson_shouldThrowException() {
        var newQuiz = new Quiz("Quiz sur les concepts fondamentaux de la programmation java", "Description de ce quiz", 80);
        var updateQuizFromLessonCommand = new UpdateQuizFromLessonCommand(idsCourse, newQuiz);
        var updateQuizFromLessonCommandHandler = new UpdateQuizFromLessonCommandHandler(courseRepository);

        var resultThrow = Assert.assertThrows(
                BadRequestException.class,
                () -> updateQuizFromLessonCommandHandler.handle(updateQuizFromLessonCommand));
        Assert.assertEquals("Lesson has not attached quiz", resultThrow.getMessage());
    }
}
