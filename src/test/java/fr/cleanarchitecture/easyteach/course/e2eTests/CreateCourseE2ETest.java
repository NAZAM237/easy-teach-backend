package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.authentication.application.ports.UserRepository;
import fr.cleanarchitecture.easyteach.authentication.domain.model.User;
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
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldCreateCourse() throws Exception {
        var user = new User();
        userRepository.save(user);
        var dto = new CreateCourseDto(
                "title",
                "description",
                BigDecimal.valueOf(1000),
                "FCFA",
                user.getUserId());

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

        var course = courseRepository.findById(courseViewModel.getNewCourse().getCourseId());

        Assert.assertTrue(course.isPresent());
        Assert.assertEquals(dto.getTitle(), course.get().getCourseTitle());
    }
}
