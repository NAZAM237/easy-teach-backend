package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.GetCourseViewModel;
import org.junit.Assert;
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
        var result = mockMvc
                .perform(MockMvcRequestBuilders.get("/courses/" + course.getCourseId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var getCourseViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                GetCourseViewModel.class
        );

        Assert.assertEquals(course.getCourseId(), getCourseViewModel.getCourseId());
    }

    @Test
    public void getNotExistingCourseE2ETest() throws Exception {
        var course = new Course(
                "title",
                "description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );

        mockMvc
                .perform(MockMvcRequestBuilders.get("/courses/" + course.getCourseId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }
}
