package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.QuestionType;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.*;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos.AnswerDto;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos.QuestionDto;
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

@RunWith(SpringRunner.class)
public class AddQuestionToQuizE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

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
    public void shouldAddQuestionToQuiz() throws Exception {
        var questionDto = new QuestionDto(
                "Explain polymorphism in POO",
                "TEXT",
                List.of(new AnswerDto("Polymorphism explanation", true)));
        mockMvc.perform(MockMvcRequestBuilders.post(
                "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/questions",
                course.getCourseId(), module.getModuleId(), lesson.getLessonId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(questionDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.questions").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.questions[0].questionText").value("Explain polymorphism in POO"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.questions[0].answers").isArray());
    }

    @Test
    public void addAlreadyExistingQuestionToQuiz_shouldThrowException() throws Exception {
        course.addQuestionToQuiz(module.getModuleId(), lesson.getLessonId(), question1);
        courseRepository.save(course);
        mockMvc.perform(MockMvcRequestBuilders.post(
                                "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/questions",
                                course.getCourseId(), module.getModuleId(), lesson.getLessonId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(question1)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
