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
public class UpdateAnswerFromQuestionE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

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
    public void shouldUpdateAnswerFromQuestionTest() throws Exception {
        var answerDto = new AnswerDto("Représentation d'un concept de la vie réelle", true);
        mockMvc.perform(MockMvcRequestBuilders.patch(
                        "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/questions/{questionId}/answers/{answerId}",
                        course.getCourseId(), module.getModuleId(), lesson.getLessonId(), question.getQuestionId(), answer.getAnswerId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(answerDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.answerText").value(answerDto.getAnswerText()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.correct").value(answerDto.isCorrect()));
    }

    @Test
    public void updateAnswerFromNotExistsQuestion_shouldThrowException() throws Exception {
        var answerDto = new AnswerDto("Représentation d'un concept de la vie réelle", true);
        mockMvc.perform(MockMvcRequestBuilders.patch(
                                "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/questions/{questionId}/answers/{answerId}",
                                course.getCourseId(), module.getModuleId(), lesson.getLessonId(), "Garbage", answer.getAnswerId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(answerDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Question not found"));
    }
}
