package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class GetCourseByIdE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void shouldGetCourseByIdE2ETest() throws Exception {
        var course = new Course(
                "title",
                "description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        courseRepository.save(course);
        mockMvc
            .perform(MockMvcRequestBuilders.get("/courses/" + course.getCourseId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.courseTitle").value(course.getCourseTitle()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.courseDescription").value(course.getCourseDescription()));
    }

    @Test
    public void getNotExistingCourseE2ETest() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/courses/Garbage"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }
}
