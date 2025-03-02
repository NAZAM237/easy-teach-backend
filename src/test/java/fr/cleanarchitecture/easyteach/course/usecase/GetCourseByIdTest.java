package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.GetCourseByIdCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.GetCourseByIdCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class GetCourseByIdTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();

    @Test
    public void getCourseByIdTest() {
        var course = new Course(
                "title",
                "description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        courseRepository.save(course);

        var getCourseByIdCommand = new GetCourseByIdCommand(course.getCourseId());
        var getCourseByIdCommandHandler = new GetCourseByIdCommandHandler(courseRepository);

        var courseViewModel = getCourseByIdCommandHandler.handle(getCourseByIdCommand);

        Assert.assertNotNull(courseViewModel);
        Assert.assertEquals(course.getCourseId(), courseViewModel.getCourseId());
    }

    @Test
    public void getNotExistingCourseByIdTest_shouldThrowException() {
        var course = new Course(
                "title",
                "description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );

        var getCourseByIdCommand = new GetCourseByIdCommand(course.getCourseId());
        var getCourseByIdCommandHandler = new GetCourseByIdCommandHandler(courseRepository);

        Assert.assertThrows(
                "Course not found",
                NotFoundException.class,
                () -> getCourseByIdCommandHandler.handle(getCourseByIdCommand)
        );
    }
}
