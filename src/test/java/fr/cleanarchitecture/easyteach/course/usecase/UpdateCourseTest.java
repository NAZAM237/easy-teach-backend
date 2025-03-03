package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.authentication.application.ports.UserRepository;
import fr.cleanarchitecture.easyteach.authentication.domain.model.User;
import fr.cleanarchitecture.easyteach.authentication.infrastructure.persistence.inmemory.InMemoryUserRepository;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.course.UpdateCourseCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.course.UpdateCourseCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class UpdateCourseTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private final UserRepository userRepository = new InMemoryUserRepository();
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
    public void updateExistingCourseTest() {
        courseRepository.save(course);

        var updateCourseCommand = new UpdateCourseCommand(course.getCourseId(), "course title2",
                "course description",
                new Price(BigDecimal.valueOf(1500), "FCFA"));
        var updateCourseCommandHandler = new UpdateCourseCommandHandler(courseRepository);

        var result = updateCourseCommandHandler.handle(updateCourseCommand);

        var newCourseUpdated = courseRepository.findByCourseId(course.getCourseId());

        Assert.assertTrue(newCourseUpdated.isPresent());
        Assert.assertEquals(result.getCourse().getCourseTitle(), newCourseUpdated.get().getCourseTitle());
        Assert.assertEquals(result.getCourse().getPrice().getAmount(), newCourseUpdated.get().getPrice().getAmount());
    }

    @Test
    public void updateNonExistingCourseTest_shouldThrowException() {
        var updateCourseCommand = new UpdateCourseCommand(course.getCourseId(), "course title2",
                "course description",
                new Price(BigDecimal.valueOf(1500), "FCFA"));
        var updateCourseCommandHandler = new UpdateCourseCommandHandler(courseRepository);

        var throwResult = Assert.assertThrows(
                NotFoundException.class,
                () -> updateCourseCommandHandler.handle(updateCourseCommand)
        );
        Assert.assertEquals("Course not found", throwResult.getMessage());
    }
}
