package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AddQuestionToQuizCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.AddQuestionToQuizCommandHandler;
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
import java.util.List;

public class AddQuestionToQuizTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();

    private Course course;
    private Lesson lesson;
    private Module module;
    private Quiz quiz;
    private Question question1;

    @Before
    public void setUp() {
        question1 = new Question(
                "Qu'est ce qu'une classe en POO",
                QuestionType.SINGLE_CHOICE,
                List.of(new Answer("Un plan de conception pour créer des objets", true),
                        new Answer("Un fichier contenant du code", false),
                        new Answer("Une fonction spécifique", false))
        );

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
        lesson.attachQuiz(quiz);
        courseRepository.save(course);
    }

    @Test
    public void addQuestionToQuizTest() {
        var newQuestion = new Question(
                "Expliquer le polymorphisme en POO",
                QuestionType.TEXT,
                List.of(new Answer("Explication Polymlorphisme", true)));
        var idsCourse = getIdsCourse();
        var addQuestionToQuizCommand = new AddQuestionToQuizCommand(idsCourse, newQuestion);
        var addQuestionToQuizCommandHandler = new AddQuestionToQuizCommandHandler(courseRepository);
        var result = addQuestionToQuizCommandHandler.handle(addQuestionToQuizCommand);
        Assert.assertEquals(1, result.getData().getQuestions().size());
    }

    @Test
    public void addAlreadyExistQuestionToQuizTest_shouldThrowException() {
        course.addQuestionToQuiz(module.getModuleId(), lesson.getLessonId(), question1);
        var idsCourse = getIdsCourse();
        var addQuestionToQuizCommand = new AddQuestionToQuizCommand(idsCourse, question1);
        var addQuestionToQuizCommandHandler = new AddQuestionToQuizCommandHandler(courseRepository);
        var resultThrow = Assert.assertThrows(
            BadRequestException.class,
            () -> addQuestionToQuizCommandHandler.handle(addQuestionToQuizCommand)
        );
        Assert.assertEquals("This question already exists in this quiz", resultThrow.getMessage());
    }

    private IdsCourse getIdsCourse() {
        return new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId(), quiz.getQuizId());
    }


}
