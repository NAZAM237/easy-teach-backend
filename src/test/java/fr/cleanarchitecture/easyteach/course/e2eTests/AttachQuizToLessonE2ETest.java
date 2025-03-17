package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.AnswerDto;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.QuestionDto;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.QuizDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
public class AttachQuizToLessonE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

    private Course course;
    private Lesson lesson;
    private fr.cleanarchitecture.easyteach.course.domain.model.Module module;
    private QuestionDto question1;
    private QuestionDto question2;
    private QuestionDto question3;

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
        question1 = new QuestionDto(
                "Qu'est ce qu'une classe en POO",
                "SINGLE_CHOICE",
                List.of(new AnswerDto("Un plan de conception pour créer des objets", true),
                        new AnswerDto("Un fichier contenant du code", false),
                        new AnswerDto("Une fonction spécifique", false))
        );
        question2 = new QuestionDto(
                "Quels sont les principes fondamentaux de la POO",
                "MULTIPLE_CHOICE",
                List.of(new AnswerDto("Encapsulation", true),
                        new AnswerDto("Héritage", true),
                        new AnswerDto("Compilation", false),
                        new AnswerDto("Polymorphisme", true))
        );
        question3 = new QuestionDto(
                "Expliquer l'encapsulation en POO",
                "TEXT",
                List.of(new AnswerDto("Explication Encapsulation", true))
        );
    }

    @Test
    public void shouldAttachQuizToLessonTest() throws Exception {
        var questions = Set.of(question1, question2, question3);
        var quizDto = new QuizDto("QuizTitle", "QuizDescription", questions, 70);

        mockMvc.perform(MockMvcRequestBuilders.post(
                "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/quiz",
                                course.getCourseId(), module.getModuleId(), lesson.getLessonId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(quizDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals(quizDto.getQuizTitle(), lesson.getQuiz().getQuizTitle());
        Assert.assertEquals(quizDto.getDescription(), lesson.getQuiz().getDescription());
        Assert.assertEquals(quizDto.getPassingScore(), lesson.getQuiz().getPassingScore());
    }

    @Test
    public void shouldAttachQuizToLessonFromNotExistCourse_shouldThrowException() throws Exception {
        var questions = Set.of(question1, question2, question3);
        var quizDto = new QuizDto("QuizTitle", "QuizDescription", questions, 70);

        mockMvc.perform(MockMvcRequestBuilders.post(
                "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/quiz",
                                "Garbage", module.getModuleId(), lesson.getLessonId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(quizDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldAttachQuizToLessonFromNotExistModule_shouldThrowException() throws Exception {
        var questions = Set.of(question1, question2, question3);
        var quizDto = new QuizDto("QuizTitle", "QuizDescription", questions, 70);

        mockMvc.perform(MockMvcRequestBuilders.post(
                                "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/quiz",
                                course.getCourseId(), "Garbage", lesson.getLessonId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(quizDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldAttachQuizToNotExistLesson_shouldThrowException() throws Exception {
        var questions = Set.of(question1, question2, question3);
        var quizDto = new QuizDto("QuizTitle", "QuizDescription", questions, 70);

        mockMvc.perform(MockMvcRequestBuilders.post(
                                "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/quiz",
                                course.getCourseId(), module.getModuleId(), "Garbage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(quizDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
