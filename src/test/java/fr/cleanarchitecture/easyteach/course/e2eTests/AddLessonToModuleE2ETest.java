package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos.AddLessonToModuleDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class AddLessonToModuleE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void shouldAddLessonToModuleE2ETest() throws Exception {
        var course = new Course(
                "courseTitle",
                "courseDescription",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        var module = new Module("title", "description", 1);
        course.addModule(module);
        courseRepository.save(course);

        var dto = new AddLessonToModuleDto("title", "IMAGES", "contentFileUrl", "textContent", 1);

        mockMvc
            .perform(MockMvcRequestBuilders.post("/courses/{courseId}/modules/{moduleId}/lessons",
                            course.getCourseId(), module.getModuleId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(dto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.lessonTitle").value("title"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.contentType").value("IMAGES"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.contentFileUrl").value("contentFileUrl"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.textContent").value("textContent"));
    }
}
