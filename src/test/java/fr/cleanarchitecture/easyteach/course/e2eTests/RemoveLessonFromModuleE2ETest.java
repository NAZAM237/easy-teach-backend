package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
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
public class RemoveLessonFromModuleE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

    private Course course;
    private Lesson lesson;
    private Module module;

    @Before
    public void setUp() {
        course = new Course(
                "courseTitle",
                "courseDescription",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        module = new Module("Introduction à JAVA", "Description", 1);
        lesson = new Lesson("Introduction", ResourceType.IMAGES, null, null, 1);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
    }

    @Test
    public void shouldRemoveLessonFromModuleE2ETest() throws Exception {
        courseRepository.save(course);
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/courses/{courseId}/modules/{moduleId}/lessons", course.getCourseId(), module.getModuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lesson.getLessonId())))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void removeLessonFromNotExistModuleE2ETest_shouldThrowException() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.patch(
                        "/courses/{courseId}/modules/{moduleId}/remove-lesson-from-module",
                                course.getCourseId(),
                                "Garbage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lesson.getLessonId())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
