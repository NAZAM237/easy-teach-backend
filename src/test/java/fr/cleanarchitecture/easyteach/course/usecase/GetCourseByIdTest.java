package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.GetCourseByIdCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.GetCourseByIdCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class GetCourseByIdTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private Course course;

    @Before
    public void setUp() {
        course = new Course(
            "title",
            "description",
            new Teacher(),
            new Price(BigDecimal.ZERO, "FCFA")
        );
        courseRepository.save(course);
    }

    @Test
    public void getCourseByIdTest() {
        var getCourseByIdCommand = new GetCourseByIdCommand(course.getCourseId());
        var getCourseByIdCommandHandler = new GetCourseByIdCommandHandler(courseRepository);

        var result = getCourseByIdCommandHandler.handle(getCourseByIdCommand);

        Assert.assertNotNull(result.getData());
        Assert.assertEquals(course.getCourseId(), result.getData().getCourseId());
    }

    @Test
    public void getNotExistingCourseByIdTest_shouldThrowException() {
        var getCourseByIdCommand = new GetCourseByIdCommand("Garbage");
        var getCourseByIdCommandHandler = new GetCourseByIdCommandHandler(courseRepository);

        Assert.assertThrows(
                "Course not found",
                NotFoundException.class,
                () -> getCourseByIdCommandHandler.handle(getCourseByIdCommand)
        );
    }
}
