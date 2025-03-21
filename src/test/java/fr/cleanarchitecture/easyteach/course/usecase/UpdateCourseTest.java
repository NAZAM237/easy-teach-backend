package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateCourseCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.UpdateCourseCommandHandler;
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
    private Course course;

    @Before
    public void setUp() {
        course = new Course(
                "course title",
                "course description",
                new Teacher(),
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

        Assert.assertEquals(result.getData().getCourseTitle(), updateCourseCommand.getCourseTitle());
        Assert.assertEquals(result.getData().getCourseDescription(), updateCourseCommand.getCourseDescription());
        Assert.assertEquals(result.getData().getPrice().getAmount(), updateCourseCommand.getPrice().getAmount());
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
