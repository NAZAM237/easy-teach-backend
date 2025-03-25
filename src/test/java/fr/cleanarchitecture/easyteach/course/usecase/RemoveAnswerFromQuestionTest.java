package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.RemoveAnswerFromQuestionCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.RemoveAnswerFromQuestionCommandHandler;
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

public class RemoveAnswerFromQuestionTest {

    private CourseRepository courseRepository = new InMemoryCourseRepository();

    private Course course;
    private Lesson lesson;
    private Module module;
    private Quiz quiz;
    private Question question;
    private Answer answer;

    @Before
    public void setUp() {
        question = new Question("Qu'est ce qu'une classe en POO", QuestionType.SINGLE_CHOICE);
        answer = new Answer("Un plan de conception pour créer des objets", true);

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
        course.addAnswerToQuestion(module.getModuleId(), lesson.getLessonId(), question.getQuestionId(), answer);
        courseRepository.save(course);
    }

    @Test
    public void removeAnswerFromQuestionTest() {
        var idsCourse = new IdsCourse(
                course.getCourseId(), module.getModuleId(),
                lesson.getLessonId(), question.getQuestionId(), answer.getAnswerId());
        var removeAnswerFromQuestionCommand = new RemoveAnswerFromQuestionCommand(idsCourse);
        var removeAnswerFromQuestionCommandHandler = new RemoveAnswerFromQuestionCommandHandler(courseRepository);

        removeAnswerFromQuestionCommandHandler.handle(removeAnswerFromQuestionCommand);

        Assert.assertTrue(question.getAnswers().isEmpty());
    }

    @Test
    public void removeNotExistAnswerFromQuestionTest_shouldThrowException() {
        var idsCourse = new IdsCourse(
                course.getCourseId(), module.getModuleId(),
                lesson.getLessonId(), question.getQuestionId(), "Garbage");
        var removeAnswerFromQuestionCommand = new RemoveAnswerFromQuestionCommand(idsCourse);
        var removeAnswerFromQuestionCommandHandler = new RemoveAnswerFromQuestionCommandHandler(courseRepository);
        var resultThrow = Assert.assertThrows(
                NotFoundException.class,
                () -> removeAnswerFromQuestionCommandHandler.handle(removeAnswerFromQuestionCommand)
        );
        Assert.assertEquals("Answer not found for this question", resultThrow.getMessage());
    }
}
