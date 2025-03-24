package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.QuestionType;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.*;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos.AnswerDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class AddAnswerToQuestionE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

    private Course course;
    private Lesson lesson;
    private Module module;
    private Quiz quiz;
    private Question question;

    @Before
    public void setUp() {
        question = new Question("Qu'est ce qu'une classe en POO", QuestionType.SINGLE_CHOICE);
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
    public void shouldAddAnswerToQuestionTest() throws Exception {
        var answer = new AnswerDto("C'est une structure de données", false);
        mockMvc.perform(MockMvcRequestBuilders.post(
                        "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/questions/{questionId}",
                        course.getCourseId(), module.getModuleId(), lesson.getLessonId(), question.getQuestionId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(answer)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.answers").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.answers").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.answers[0].answerText").value("C'est une structure de données"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.answers[0].correct").value(false));
    }

    @Test
    public void shouldAddAnswerToNotExistsQuestion_shouldThrowException() throws Exception {
        var answer = new AnswerDto("C'est une structure de données", false);
        mockMvc.perform(MockMvcRequestBuilders.post(
                                "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/questions/{questionId}",
                                course.getCourseId(), module.getModuleId(), lesson.getLessonId(), "Garbage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(answer)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
