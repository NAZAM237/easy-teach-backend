package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos.AddModuleToCourseDto;
import org.junit.Assert;
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
                    MockMvcRequestBuilders.patch("/courses/" +course.getCourseId()+ "/add-module-to-course")
                            .content(objectMapper.writeValueAsBytes(dto))
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        var existingCourse = courseRepository.findByCourseId(course.getCourseId());

        Assert.assertTrue(existingCourse.isPresent());
        Assert.assertEquals(1, existingCourse.get().getModules().size());
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
