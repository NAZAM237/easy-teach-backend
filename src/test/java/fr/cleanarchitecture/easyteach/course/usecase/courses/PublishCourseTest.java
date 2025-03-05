package fr.cleanarchitecture.easyteach.course.usecase.courses;

import fr.cleanarchitecture.easyteach.authentication.application.ports.UserRepository;
import fr.cleanarchitecture.easyteach.authentication.domain.model.User;
import fr.cleanarchitecture.easyteach.authentication.infrastructure.persistence.inmemory.InMemoryUserRepository;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.course.PublishCourseCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.course.PublishCourseCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.CourseStatus;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class PublishCourseTest {

    private CourseRepository courseRepository = new InMemoryCourseRepository();
    private UserRepository userRepository = new InMemoryUserRepository();
    private Course course;
    private User user;

    @Before
    public void setUp() {
        user = new User();
        userRepository.save(user);
        course = new Course(
                "course title",
                "course description",
                new Teacher(
                        user.getUserId(),
                        user.getUserName(),
                        user.getUserBiography(),
                        user.getUserEmail(),
                        user.getUserPhone(),
                        user.getUserPhoto()
                ),
                new Price(BigDecimal.valueOf(1000), "FCFA")
        );
    }

    @Test
    public void publishCourseTest() {
        course.addModule(new Module(1));
        courseRepository.save(course);

        var publishCourseCommand = new PublishCourseCommand(course.getCourseId());
        var publishCourseCommandHandler = new PublishCourseCommandHandler(courseRepository);

        var result = publishCourseCommandHandler.handle(publishCourseCommand);
        var existingCourse = courseRepository.findByCourseId(course.getCourseId());

        Assert.assertTrue(existingCourse.isPresent());
        Assert.assertEquals(CourseStatus.PUBLISHED, existingCourse.get().getStatus());
        Assert.assertEquals(CourseStatus.PUBLISHED, result.getCourse().getStatus());
    }

    @Test
    public void publishUnExistingCourseTest_shouldThrowException() {
        var publishCourseCommand = new PublishCourseCommand(course.getCourseId());
        var publishCourseCommandHandler = new PublishCourseCommandHandler(courseRepository);

        Assert.assertThrows(
                "Course not found",
                NotFoundException.class,
                () -> publishCourseCommandHandler.handle(publishCourseCommand)
        );
    }
}
