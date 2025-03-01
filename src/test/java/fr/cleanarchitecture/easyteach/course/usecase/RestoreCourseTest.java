package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.authentication.application.ports.UserRepository;
import fr.cleanarchitecture.easyteach.authentication.domain.model.User;
import fr.cleanarchitecture.easyteach.authentication.infrastructure.persistence.inmemory.InMemoryUserRepository;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.RestoreCourseCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.RestoreCourseCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class RestoreCourseTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private final UserRepository userRepository = new InMemoryUserRepository();
    private Course course;
    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User();
        userRepository.save(user);
        course = new Course(
                "Course title",
                "Course description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
    }

    @Test
    public void restoreCourseTest() {
        course.addModule(new Module(1));
        course.publish();
        course.archive();
        courseRepository.save(course);

        var restoreCourseCommand = new RestoreCourseCommand(course.getCourseId());
        var restoreCourseCommandHandler = new RestoreCourseCommandHandler(courseRepository);

        var result = restoreCourseCommandHandler.handle(restoreCourseCommand);

        var archivedCourse = courseRepository.findByCourseId(course.getCourseId());

        Assert.assertTrue(archivedCourse.isPresent());
        Assert.assertEquals(result.getCourse().getStatus(), archivedCourse.get().getStatus());
    }

    @Test
    public void restoreUnExistingCourseTest_shouldThrowException() {
        var restoreCourseCommand = new RestoreCourseCommand(course.getCourseId());
        var restoreCourseCommandHandler = new RestoreCourseCommandHandler(courseRepository);
        Assert.assertThrows(
                "Course not found",
                NotFoundException.class,
                () -> restoreCourseCommandHandler.handle(restoreCourseCommand)
        );
    }

    @Test
    public void restoreNotArchivedCourseTest_shouldThrowException() {
        courseRepository.save(course);
        var restoreCourseCommand = new RestoreCourseCommand(course.getCourseId());
        var restoreCourseCommandHandler = new RestoreCourseCommandHandler(courseRepository);
        Assert.assertThrows(
                "The status of the course is not archived",
                BadRequestException.class,
                () -> restoreCourseCommandHandler.handle(restoreCourseCommand)
        );
    }
}
