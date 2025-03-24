package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.*;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class DetachQuizFromLessonE2ETest extends EasyTeachIntegrationTests {

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
        course.attachQuizToLesson(module.getModuleId(), lesson.getLessonId(), quiz);
        courseRepository.save(course);
    }

    @Test
    public void shouldDetachQuizFromLessonTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(
                    "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/quiz",
                    course.getCourseId(), module.getModuleId(), lesson.getLessonId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void detachQuizFromNotExistsLesson_shouldThrowException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(
                        "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/quiz",
                        course.getCourseId(), module.getModuleId(), "Garbage"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Lesson not found"));
    }
}
