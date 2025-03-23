package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateQuestionFromQuizCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.UpdateQuestionFromQuizCommandHandler;
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

public class UpdateQuestionFromQuizTest {

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
        course.attachQuizToLesson(module.getModuleId(), lesson.getLessonId(), quiz);
        course.addQuestionToQuiz(module.getModuleId(), lesson.getLessonId(), question);
        courseRepository.save(course);
    }

    @Test
    public void updateQuestionFromQuizTest() {
        var idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId(), question.getQuestionId());
        var newQuestion = new Question(
                "Qu'est ce qu'un objet en POO",
                QuestionType.TEXT,
                List.of(new Answer("TEXT EXPLANATION", true)));
        var updateQuestionFromQuizCommand = new UpdateQuestionFromQuizCommand(idsCourse, newQuestion);
        var updateQuestionFromQuizCommandHandler = new UpdateQuestionFromQuizCommandHandler(courseRepository);

        var result = updateQuestionFromQuizCommandHandler.handle(updateQuestionFromQuizCommand);

        Assert.assertEquals(newQuestion.getQuestionText(), result.getData().getQuestionText());
        Assert.assertEquals(newQuestion.getQuestionType(), result.getData().getQuestionType());
    }
}
