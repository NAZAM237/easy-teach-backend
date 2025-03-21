package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos.UpdateModuleDto;
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

        mockMvc
            .perform(MockMvcRequestBuilders.patch(
                    "/courses/{courseId}/modules/{moduleId}",
                            course.getCourseId(),
                            module.getModuleId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateModuleDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.moduleTitle").value(updateModuleDto.getModuleTitle()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.moduleDescription").value(updateModuleDto.getModuleDescription()));
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
