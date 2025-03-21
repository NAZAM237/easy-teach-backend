package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class GetAllCourseE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

    @Before
    public void setUp() {
        courseRepository.clear();
    }

    @Test
    public void shouldGetAllCourseE2ETest() throws Exception {
        var course1 = new Course(
                "title1",
                "description1",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        var course2 = new Course(
                "title2",
                "description2",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        courseRepository.save(course1);
        courseRepository.save(course2);

        mockMvc.perform(MockMvcRequestBuilders.get("/courses"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].courseTitle").value(course2.getCourseTitle()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].courseDescription").value(course2.getCourseDescription()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].courseTitle").value(course1.getCourseTitle()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].courseDescription").value(course1.getCourseDescription()));
    }
}
