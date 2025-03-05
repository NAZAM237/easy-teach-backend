package fr.cleanarchitecture.easyteach.course.e2eTests.courses;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.authentication.application.ports.UserRepository;
import fr.cleanarchitecture.easyteach.authentication.domain.model.User;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class DeleteCourseE2ETest extends EasyTeachIntegrationTests {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void deleteCourseTest() throws Exception {
        var user = new User();
        userRepository.save(user);
        var course = new Course(
                "title",
                "description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        courseRepository.save(course);
        mockMvc
            .perform(MockMvcRequestBuilders.delete("/courses/" + course.getCourseId()))
                    //.header("Authorization", createJwt())
            .andExpect(MockMvcResultMatchers.status().isNoContent());
        var deletedCourse = courseRepository.findByCourseId(course.getCourseId());
        Assert.assertTrue(deletedCourse.isEmpty());
    }

    @Test
    public void deleteUnexistingCourseTest_shouldThrow() throws Exception {
        var user = new User();
        userRepository.save(user);
        var course = new Course(
                "title",
                "description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        mockMvc
            .perform(MockMvcRequestBuilders.delete("/courses/" + course.getCourseId()))
            //.header("Authorization", createJwt())
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteExistingCourseWithStatusPublishedTest_shouldThrow() throws Exception {
        var user = new User();
        userRepository.save(user);
        var course = new Course(
                "title",
                "description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        course.addModule(new Module(1));
        course.publish();
        courseRepository.save(course);
        mockMvc
            .perform(MockMvcRequestBuilders.delete("/courses/" + course.getCourseId()))
            //.header("Authorization", createJwt())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
