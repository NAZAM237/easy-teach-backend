package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos.AddModuleToCourseDto;
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
public class AddModuleToCourseE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;
    private Course course;
    private AddModuleToCourseDto dto;

    @Before
    public void setUp() {
        course = new Course(
            "Title",
            "Description",
            new Teacher(),
            new Price(BigDecimal.ZERO, "FCFA")
        );
        courseRepository.save(course);

        dto = new AddModuleToCourseDto(
            "module title",
            "Module description",
            1);
    }

    @Test
    public void addModuleToCourseE2ETest() throws Exception {
        mockMvc
                .perform(
                    MockMvcRequestBuilders.post("/courses/" +course.getCourseId()+ "/modules")
                            .content(objectMapper.writeValueAsBytes(dto))
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.moduleTitle").value(dto.getModuleTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.moduleDescription").value(dto.getModuleDescription()));
    }

    @Test
    public void addModuleToNotExistCourseE2ETest_shouldThrowException() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders.patch("/courses/Garbage/add-module-to-course")
                                .content(objectMapper.writeValueAsBytes(dto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
