package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.authentication.application.ports.UserRepository;
import fr.cleanarchitecture.easyteach.authentication.domain.model.User;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.CourseStatus;
import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.CourseViewModel;
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
public class PublishCourseE2ETest extends EasyTeachIntegrationTests {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldPublishExistingCourseTest() throws Exception {
        var user = new User();
        userRepository.save(user);
        var course = new Course(
                "course title",
                "course description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA"));
        var module = new Module("moduleTitle", "moduleDescription",1);
        course.addModule(module);
        course.addLessonToModule(
                module.getModuleId(),
                new Lesson("lessonTitle", LessonType.TEXT, null, "textContent", 1)
        );
        courseRepository.save(course);

        var result = mockMvc
                .perform(MockMvcRequestBuilders.patch("/courses/" + course.getCourseId() + "/publish"))
                        //.header("Authorization", createJwt()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var courseViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CourseViewModel.class);

        var existingCourse = courseRepository.findByCourseId(courseViewModel.getCourse().getCourseId());

        Assert.assertTrue(existingCourse.isPresent());
        Assert.assertEquals(CourseStatus.PUBLISHED, courseViewModel.getCourse().getStatus());
    }

    @Test
    public void publishUnExistingCourseTest_shouldThrowException() throws Exception {
        var user = new User();
        userRepository.save(user);
        var course = new Course(
                "course title",
                "course description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA"));

        mockMvc
                .perform(MockMvcRequestBuilders.patch("/courses/" + course.getCourseId() + "/publish")
                        //.header("Authorization", createJwt())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }
}
