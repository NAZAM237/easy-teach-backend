package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
public class GetAllCourseE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

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

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/courses/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var courses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                List.class
        );

        Assert.assertFalse(courses.isEmpty());
        Assert.assertEquals(2, courses.size());
    }
}
