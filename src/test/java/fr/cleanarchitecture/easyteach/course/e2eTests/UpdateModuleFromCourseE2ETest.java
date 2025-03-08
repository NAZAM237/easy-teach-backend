package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.UpdateModuleDto;
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
public class UpdateModuleFromCourseE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void shouldUpdateModuleFromCourseE2ETest() throws Exception {
        var course = new Course(
                "courseTitle",
                "courseDescription",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        var module = new Module("Module title", "module description", 1);
        course.addModule(module);
        courseRepository.save(course);

        var updateModuleDto = new UpdateModuleDto("title", "description");

        var result = mockMvc
                .perform(MockMvcRequestBuilders.patch(
                        "/courses/{courseId}/modules/{moduleId}",
                                course.getCourseId(),
                                module.getModuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updateModuleDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var courseViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CourseViewModel.class);

        var updatedCourse = courseRepository.findByCourseId(courseViewModel.getCourse().getCourseId()).orElseThrow();
        var updatedModule = updatedCourse.getModules().stream()
                .filter(m -> m.getModuleId().equals(module.getModuleId()))
                .findFirst()
                .orElseThrow();

        Assert.assertEquals("title", updatedModule.getModuleTitle());
        Assert.assertEquals("description", updatedModule.getModuleDescription());
    }

    @Test
    public void updateNotExistingModuleE2ETest_shouldThrowException() throws Exception {
        var course = new Course(
                "courseTitle",
                "courseDescription",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        var updateModuleDto = new UpdateModuleDto("title", "description");

        mockMvc
            .perform(MockMvcRequestBuilders.patch("/courses/{courseId}/modules/{moduleId}", course.getCourseId(), "garbage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateModuleDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
