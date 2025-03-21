package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.authentication.application.ports.UserRepository;
import fr.cleanarchitecture.easyteach.authentication.domain.model.User;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos.CreateCourseDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
public class CreateCourseE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        courseRepository.clear();
    }

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

        mockMvc
            .perform(MockMvcRequestBuilders.post("/courses")
                    //.header("Authorization", createJwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(jsonPath("$.message").value("A new course was created successfully"))
            .andExpect(jsonPath("$.course.courseTitle").value(dto.getTitle()))
            .andExpect(jsonPath("$.course.courseDescription").value(dto.getDescription()));
    }
}
