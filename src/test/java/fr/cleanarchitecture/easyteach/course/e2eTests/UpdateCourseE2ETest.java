package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos.UpdateCourseDto;
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
public class UpdateCourseE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void shouldUpdateCourse() throws Exception {
        var course = new Course(
                "course title",
                "course description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA"));
        courseRepository.save(course);

        var dto = new UpdateCourseDto(
                "title2",
                "description2",
                BigDecimal.valueOf(1000),
                "FCFA");

        var result = mockMvc
                .perform(MockMvcRequestBuilders.patch("/courses/" + course.getCourseId())
                        //.header("Authorization", createJwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var courseViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CourseViewModel.class);

        var existingCourse = courseRepository.findByCourseId(courseViewModel.getCourse().getCourseId());

        Assert.assertTrue(existingCourse.isPresent());
        Assert.assertEquals(dto.getCourseTitle(), existingCourse.get().getCourseTitle());
        Assert.assertEquals(dto.getCourseDescription(), existingCourse.get().getCourseDescription());
    }

    @Test
    public void updateUnExistingCourseTest_shouldThrow() throws Exception {
        var course = new Course(
                "course title",
                "course description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA"));

        var dto = new UpdateCourseDto(
                "title2",
                "description2",
                BigDecimal.valueOf(1000),
                "FCFA");

        mockMvc
            .perform(MockMvcRequestBuilders.patch("/courses/" + course.getCourseId(), dto)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
    }
}
