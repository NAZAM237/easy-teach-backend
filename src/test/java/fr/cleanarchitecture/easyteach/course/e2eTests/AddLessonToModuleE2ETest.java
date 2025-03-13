package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.AddLessonToModuleDto;
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

        var dto = new AddLessonToModuleDto("title", "IMAGES", "videoUrl", "textContent", 1);

        var result = mockMvc
                .perform(MockMvcRequestBuilders.patch("/courses/{courseId}/modules/{moduleId}/add-lesson-to-module",
                                course.getCourseId(), module.getModuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var courseViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CourseViewModel.class
        );

        var updatedCourse = courseRepository.findByCourseId(courseViewModel.getCourse().getCourseId()).orElseThrow();
        var updatedModule = updatedCourse.getModules().stream()
                .filter(module1 -> module1.getModuleId().equals(module.getModuleId()))
                .findFirst()
                .orElseThrow();
        Assert.assertNotNull(updatedModule);
        Assert.assertEquals(1, updatedModule.getLessons().size());
    }
}
