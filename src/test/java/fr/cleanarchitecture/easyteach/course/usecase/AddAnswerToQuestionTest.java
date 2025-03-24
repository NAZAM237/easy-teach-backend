package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AddAnswerToQuestionCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.AddAnswerToQuestionCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.QuestionType;
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

public class AddAnswerToQuestionTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();

    private Course course;
    private Lesson lesson;
    private Module module;
    private Quiz quiz;
    private Question question;

    @Before
    public void setUp() {
        question = new Question(
                "Qu'est ce qu'une classe en POO",
                QuestionType.SINGLE_CHOICE);

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
        courseRepository.save(course);
    }

    @Test
    public void addAnswerToQuestionTest() {
        var idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId(), question.getQuestionId());
        var answer = new Answer("Un plan de conception pour créer des objets", true);
        var addAnswerToQuestionCommand = new AddAnswerToQuestionCommand(idsCourse, answer);
        var addAnswerToQuestionCommandHandler = new AddAnswerToQuestionCommandHandler(courseRepository);

        var result = addAnswerToQuestionCommandHandler.handle(addAnswerToQuestionCommand);

        Assert.assertEquals(1, question.getAnswers().size());
        Assert.assertTrue(result.getData().getAnswers().contains(answer));
    }

    @Test
    public void addExistingAnswerToQuestionTest_shouldThrowException() {
        var answer = new Answer("Un plan de conception pour créer des objets", true);
        course.addAnswerToQuestion(module.getModuleId(), lesson.getLessonId(), question.getQuestionId(), answer);
        var idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId(), question.getQuestionId());
        var addAnswerToQuestionCommand = new AddAnswerToQuestionCommand(idsCourse, answer);
        var addAnswerToQuestionCommandHandler = new AddAnswerToQuestionCommandHandler(courseRepository);

        var resultThrow = Assert.assertThrows(
                BadRequestException.class,
                () -> addAnswerToQuestionCommandHandler.handle(addAnswerToQuestionCommand)
        );
        Assert.assertEquals("Answer already exists in this question", resultThrow.getMessage());
    }
}
