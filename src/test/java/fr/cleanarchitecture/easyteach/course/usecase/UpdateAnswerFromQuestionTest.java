package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateAnswerFromQuestionCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.UpdateAnswerFromQuestionCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.QuestionType;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.*;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class UpdateAnswerFromQuestionTest {

    private CourseRepository courseRepository = new InMemoryCourseRepository();

    private Course course;
    private Lesson lesson;
    private Module module;
    private Quiz quiz;
    private Question question;
    private Answer answer1;
    private Answer answer2;

    @Before
    public void setUp() {
        question = new Question("Qu'est ce qu'une classe en POO", QuestionType.SINGLE_CHOICE);
        answer1 = new Answer("Un plan de conception pour créer des objets", true);
        answer2 = new Answer("Un fichier contenant du code", false);

        course = new Course("Apprenez à programmer en Java", "Description...", new Teacher(), new Price(BigDecimal.ZERO, "FCFA"));
        module = new Module("Introduction à Java", "Description module...", 1);
        lesson = new Lesson("Introduction", ResourceType.DOCUMENTS, null, "TextContent", 1);
        quiz = new Quiz(
                "Quiz sur le POO",
                "Ce quiz teste vos connaissances de base sur la POO",
                70
        );
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
        course.attachQuizToLesson(module.getModuleId(), lesson.getLessonId(), quiz);
        course.addQuestionToQuiz(module.getModuleId(), lesson.getLessonId(), question);
        course.addAnswerToQuestion(module.getModuleId(), lesson.getLessonId(), question.getQuestionId(), answer1);
        course.addAnswerToQuestion(module.getModuleId(), lesson.getLessonId(), question.getQuestionId(), answer2);
        courseRepository.save(course);
    }

    @Test
    public void updateAnswerFromQuestionTest() {
        var idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId(), question.getQuestionId(), answer1.getAnswerId());
        var updateAnswerFromQuestionCommand = new UpdateAnswerFromQuestionCommand(idsCourse, "newAnswer", false);
        var updateAnswerFromQuestionCommandHandler = new UpdateAnswerFromQuestionCommandHandler(courseRepository);

        var result = updateAnswerFromQuestionCommandHandler.handle(updateAnswerFromQuestionCommand);

        Assert.assertEquals(updateAnswerFromQuestionCommand.getAnswerText(), result.getData().getAnswerText());
        Assert.assertEquals(updateAnswerFromQuestionCommand.isCorrectAnswer(), result.getData().isCorrect());
    }

    @Test
    public void updateNotExistsAnswerFromQuestionTest_shouldThrowException() {
        var idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId(), question.getQuestionId(), "Garbage");
        var updateAnswerFromQuestionCommand = new UpdateAnswerFromQuestionCommand(idsCourse, "newAnswer", false);
        var updateAnswerFromQuestionCommandHandler = new UpdateAnswerFromQuestionCommandHandler(courseRepository);

        var resultThrows = Assert.assertThrows(
                NotFoundException.class,
                () -> updateAnswerFromQuestionCommandHandler.handle(updateAnswerFromQuestionCommand)
        );
        Assert.assertEquals("Answer not found for this question", resultThrows.getMessage());
    }
}
