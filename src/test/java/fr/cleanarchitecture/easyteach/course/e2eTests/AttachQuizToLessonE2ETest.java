package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos.QuizDto;
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

@RunWith(SpringRunner.class)
public class AttachQuizToLessonE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

    private Course course;
    private Lesson lesson;
    private Module module;

    @Before
    public void setUp() {
        course = new Course("Apprenez à programmer en Java", "Description...", new Teacher(), new Price(BigDecimal.ZERO, "FCFA"));
        module = new Module("Introduction à Java", "Description module...", 1);
        lesson = new Lesson("Introduction", ResourceType.DOCUMENTS, null, "TextContent", 1);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
        courseRepository.save(course);
    }

    @Test
    public void shouldAttachQuizToLessonTest() throws Exception {
        var quizDto = new QuizDto("QuizTitle", "QuizDescription", 70);

        mockMvc.perform(MockMvcRequestBuilders.post(
                "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/quiz",
                                course.getCourseId(), module.getModuleId(), lesson.getLessonId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(quizDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals(quizDto.getQuizTitle(), lesson.getQuiz().getQuizTitle());
        Assert.assertEquals(quizDto.getQuizDescription(), lesson.getQuiz().getDescription());
        Assert.assertEquals(quizDto.getPassingScore(), lesson.getQuiz().getPassingScore());
    }

    @Test
    public void shouldAttachQuizToLessonFromNotExistCourse_shouldThrowException() throws Exception {
        var quizDto = new QuizDto("QuizTitle", "QuizDescription", 70);

        mockMvc.perform(MockMvcRequestBuilders.post(
                "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/quiz",
                                "Garbage", module.getModuleId(), lesson.getLessonId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(quizDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldAttachQuizToLessonFromNotExistModule_shouldThrowException() throws Exception {
        var quizDto = new QuizDto("QuizTitle", "QuizDescription", 70);

        mockMvc.perform(MockMvcRequestBuilders.post(
                                "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/quiz",
                                course.getCourseId(), "Garbage", lesson.getLessonId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(quizDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldAttachQuizToNotExistLesson_shouldThrowException() throws Exception {
        var quizDto = new QuizDto("QuizTitle", "QuizDescription", 70);

        mockMvc.perform(MockMvcRequestBuilders.post(
                                "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/quiz",
                                course.getCourseId(), module.getModuleId(), "Garbage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(quizDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
