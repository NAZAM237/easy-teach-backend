package fr.cleanarchitecture.easyteach.course.e2eTests.courses;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleFromCourseViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class GetAllModulesFromCourseE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void shouldGetAllModulesFromCourseE2ETest() throws Exception {
        var course = new Course(
                "title",
                "description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        course.addModule(new Module("title", "description", 1));
        course.addModule(new Module("title2", "description2", 2));
        course.addModule(new Module("titl3", "description", 3));
        courseRepository.save(course);

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}/modules", course.getCourseId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var module = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ModuleFromCourseViewModel.class);

        Assert.assertNotNull(module);
        Assert.assertEquals(course.getCourseId(), module.getCourseId());
        Assert.assertEquals(3, module.getModules().size());
    }

    @Test
    public void getAllModulesFromUnExistingCourseE2ETest_shouldThrowException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}/modules", "garbage"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
