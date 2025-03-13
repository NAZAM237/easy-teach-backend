package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.authentication.application.ports.UserRepository;
import fr.cleanarchitecture.easyteach.authentication.domain.model.User;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.CourseStatus;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;
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
public class ArchiveCourseE2ETest extends EasyTeachIntegrationTests {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;
    private Course course;

    @Before
    public void setUp() {
        user = new User();
        this.userRepository.save(user);
        course = new Course(
                "course title",
                "course description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
    }

    @Test
    public void shouldArchiveCourseTest() throws Exception {
        var module = new Module("moduleTitle", "moduleDescription",1);
        course.addModule(module);
        course.addLessonToModule(
                module.getModuleId(),
                new Lesson("lessonTitle", ResourceType.IMAGES, null, "textConent", 1)
        );
        course.publish();
        courseRepository.save(course);

        var result = mockMvc
                .perform(MockMvcRequestBuilders.patch("/courses/" + course.getCourseId() + "/archive"))
                //.header("Authorization", createJwt()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var courseViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CourseViewModel.class);

        var archivedCourse = courseRepository.findByCourseId(courseViewModel.getCourse().getCourseId());

        Assert.assertTrue(archivedCourse.isPresent());
        Assert.assertEquals(CourseStatus.ARCHIVED, courseViewModel.getCourse().getStatus());
    }

    @Test
    public void archiveUnExistingCourseTest_shouldThrowException() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/courses/" + course.getCourseId() + "/archive")
                        //.header("Authorization", createJwt())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }
}
