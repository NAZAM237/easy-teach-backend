package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
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

    @Before
    public void setUp() {
        courseRepository.clear();
    }

    @Test
    public void shouldCreateCourse() throws Exception {
        var dto = new CreateCourseDto(
                "title",
                "description",
                BigDecimal.valueOf(1000),
                "FCFA");

        mockMvc
            .perform(MockMvcRequestBuilders.post("/courses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(jsonPath("$.message").value("success"))
            .andExpect(jsonPath("$.data.courseTitle").value(dto.getTitle()))
            .andExpect(jsonPath("$.data.courseDescription").value(dto.getDescription()));
    }
}
