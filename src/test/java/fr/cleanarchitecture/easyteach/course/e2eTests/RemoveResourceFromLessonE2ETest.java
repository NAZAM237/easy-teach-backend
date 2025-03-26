package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.*;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class RemoveResourceFromLessonE2ETest extends EasyTeachIntegrationTests {

    private static final String UPLOAD_DIR = "/Users/nazam/Desktop/Projects/easy-teach-backend/uploaded-resources";
    private static final String RESOURCE_URL = UPLOAD_DIR + "/images/file.png";

    @Autowired
    private CourseRepository courseRepository;

    private Course course;
    private Module module;
    private Lesson lesson;
    private Resource resource;

    @Before
    public void setUp() throws Exception {
        course = new Course(
                "courseTitle",
                "courseDescription",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA"));
        module = new Module("moduleTitle", "moduleDescription", 1);
        lesson = new Lesson("lessonTitle", ResourceType.IMAGES, null, "textContent", 1);
        resource = new Resource("resourceName", RESOURCE_URL);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
        lesson.addResource(resource);
        courseRepository.save(course);
    }

    @Test
    public void shouldRemoveResourceFromLessonTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(
                    "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/resources/{resourceId}",
                    course.getCourseId(), module.getModuleId(), lesson.getLessonId(), resource.getResourceId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assert.assertTrue(lesson.getResources().isEmpty());
        Assert.assertFalse(new File(resource.getResourceUrl()).exists());
    }
}
