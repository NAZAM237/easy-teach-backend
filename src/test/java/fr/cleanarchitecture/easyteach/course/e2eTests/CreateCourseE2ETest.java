package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.CreateCourseDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class CreateCourseE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void shouldCreateCourse() throws Exception {
        var dto = new CreateCourseDto(
                "title",
                "description",
                BigDecimal.valueOf(1000),
                "FCFA");

        var result = mockMvc
                .perform(MockMvcRequestBuilders.post("/courses")
                        //.header("Authorization", createJwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        var courseViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CourseViewModel.class);

        var course = courseRepository.findById(courseViewModel.getCourseId());

        Assert.assertTrue(course.isPresent());
        Assert.assertEquals(dto.getTitle(), course.get().getCourseTitle());
    }
}
