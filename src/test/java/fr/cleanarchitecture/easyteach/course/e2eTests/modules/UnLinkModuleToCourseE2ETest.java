package fr.cleanarchitecture.easyteach.course.e2eTests.modules;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.ModuleViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class UnLinkModuleToCourseE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModuleRepository moduleRepository;

    @BeforeEach
    public void setup() {
        courseRepository.clear();
    }

    @Test
    public void shouldUnLinkModuleToCourseE2ETest() throws Exception {
        var course = new Course(
                "title",
                "description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        var module = new Module(
                "title",
                "description",
                1
        );
        course.addModule(module);
        moduleRepository.save(module);
        courseRepository.save(course);

        var result = mockMvc
                .perform(MockMvcRequestBuilders.patch(
                        "/modules/{moduleId}/courses/{courseId}/unlink", module.getModuleId(), course.getCourseId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var moduleViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ModuleViewModel.class
        );

        Assert.assertNotNull(moduleViewModel);
        Assert.assertEquals(module.getModuleId(), moduleViewModel.getModuleId());
        Assert.assertFalse(moduleViewModel.isLinkToCourse());
    }

    @Test
    public void unLinkModuleToUnExistingCourseE2ETest_shouldThrowException() throws Exception {
        var module = new Module(
                "title",
                "description",
                1
        );
        moduleRepository.save(module);

        mockMvc
            .perform(MockMvcRequestBuilders.patch(
                            "/modules/{moduleId}/courses/{courseId}/unlink", module.getModuleId(), "Garbage")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void UnLinkAlwaysNotLinkedModuleToCourseE2ETest_shouldThrowException() throws Exception {
        var course = new Course(
                "title",
                "description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        var module = new Module(
                "title",
                "description",
                1
        );
        moduleRepository.save(module);
        courseRepository.save(course);

        mockMvc
                .perform(MockMvcRequestBuilders.patch(
                                "/modules/{moduleId}/courses/{courseId}/unlink", module.getModuleId(), course.getCourseId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
