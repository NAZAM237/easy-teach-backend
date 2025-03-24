package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.*;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos.QuizDto;
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
public class UpdateQuizFromLessonE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

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
    public void shouldUpdateQuizFromLessonTest() throws Exception {
        course.attachQuizToLesson(module.getModuleId(), lesson.getLessonId(), quiz);
        var newQuiz = new QuizDto("Quiz sur les fondements de la POO", "Description quiz", 80);
        mockMvc.perform(MockMvcRequestBuilders.patch(
                        "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/quiz",
                        course.getCourseId(), module.getModuleId(), lesson.getLessonId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(newQuiz)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.quizTitle").value(newQuiz.getQuizTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(newQuiz.getQuizDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.passingScore").value(newQuiz.getPassingScore()));
    }

    @Test
    public void shouldUpdateQuizFromNotExistsLesson_shouldThrowException() throws Exception {
        course.attachQuizToLesson(module.getModuleId(), lesson.getLessonId(), quiz);
        var newQuiz = new QuizDto("Quiz sur les fondements de la POO", "Description quiz", 80);
        mockMvc.perform(MockMvcRequestBuilders.patch(
                                "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/quiz",
                                course.getCourseId(), module.getModuleId(), "Garbage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(newQuiz)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
