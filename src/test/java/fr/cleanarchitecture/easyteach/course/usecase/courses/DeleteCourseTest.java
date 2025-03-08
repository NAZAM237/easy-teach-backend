package fr.cleanarchitecture.easyteach.course.usecase.courses;

import fr.cleanarchitecture.easyteach.authentication.application.ports.UserRepository;
import fr.cleanarchitecture.easyteach.authentication.domain.model.User;
import fr.cleanarchitecture.easyteach.authentication.infrastructure.persistence.inmemory.InMemoryUserRepository;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.course.DeleteCourseCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.course.DeleteCourseCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class DeleteCourseTest {

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
    public void deleteExistingCourseTest() {
        courseRepository.save(course);

        var deleteCourseCommand = new DeleteCourseCommand(course.getCourseId());
        var deleteCourseCommandHandler = new DeleteCourseCommandHandler(courseRepository);
        deleteCourseCommandHandler.handle(deleteCourseCommand);

        var courseDeleted = courseRepository.findByCourseId(course.getCourseId());
        Assert.assertTrue(courseDeleted.isEmpty());
    }

    @Test
    public void deleteNonExistingCourseTest_shouldThrowException() {
        var deleteCourseCommand = new DeleteCourseCommand(course.getCourseId());
        var deleteCourseCommandHandler = new DeleteCourseCommandHandler(courseRepository);

        Assert.assertThrows(
                "Course not found",
                NotFoundException.class,
                () -> deleteCourseCommandHandler.handle(deleteCourseCommand)
        );
    }

    @Test
    public void deleteCourseWithStatusPublishedTest_shouldThrowException() {
        var module = new Module("moduleTitle", "moduleDescription",1);
        course.addModule(module);
        course.addLessonToModule(
                module.getModuleId(),
                new Lesson("lessonTitle", LessonType.TEXT, null, "textContent", 1));
        course.publish();
        courseRepository.save(course);
        var deleteCourseCommand = new DeleteCourseCommand(course.getCourseId());
        var deleteCourseCommandHandler = new DeleteCourseCommandHandler(courseRepository);

        Assert.assertThrows(
                "Unable to delete. Course must be draft or archived to be deleted",
                BadRequestException.class,
                () -> deleteCourseCommandHandler.handle(deleteCourseCommand)
        );
    }
}
