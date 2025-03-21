package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.CourseStatus;
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
public class RestoreCourseE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;
    private Course course;

    @Before
    public void setUp() {
        course = new Course(
                "course title",
                "course description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
    }

    @Test
    public void shouldRestoreCourseTest() throws Exception {
        var module =new Module("moduleTitle", "moduleDescription",1);
        course.addModule(module);
        course.addLessonToModule(
                module.getModuleId(),
                new Lesson("lessonTitle", ResourceType.IMAGES, "Images", "textConent", 1)
        );
        course.publish();
        course.archive();
        courseRepository.save(course);

        mockMvc
            .perform(MockMvcRequestBuilders.patch("/courses/" + course.getCourseId() + "/restore"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(CourseStatus.DRAFT.name()));
    }

    @Test
    public void archiveUnExistingCourseTest_shouldThrowException() throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders.patch("/courses/" + course.getCourseId() + "/restore")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
