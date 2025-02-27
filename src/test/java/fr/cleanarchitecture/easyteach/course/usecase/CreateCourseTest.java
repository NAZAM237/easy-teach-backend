package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.CreateCourseCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.CreateCourseCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.StatusEnum;
import fr.cleanarchitecture.easyteach.course.domain.model.Instructor;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class CreateCourseTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();

    @Test
    public void shouldCreateCourse() {
        var createCourseCommand = new CreateCourseCommand(
                "course name",
                "course description",
                new Instructor(),
                new Price(BigDecimal.valueOf(1000), "FCFA"));
        var createCourseCommandHandler = new CreateCourseCommandHandler(courseRepository);

        var result = createCourseCommandHandler.handle(createCourseCommand);

        var newCourseCreated = courseRepository.findById(result.getCourseId());

        Assert.assertTrue(newCourseCreated.isPresent());
        Assert.assertEquals(result.getCourseId(), newCourseCreated.get().getCourseId());
        Assert.assertEquals(StatusEnum.DRAFT, newCourseCreated.get().getStatus());
    }

    @Test
    public void createCourseIfCourseAlreadyExists_shouldThrowException() {
        var createCourseCommand = new CreateCourseCommand(
                "course name",
                "course description",
                new Instructor(),
                new Price(BigDecimal.valueOf(1000), "FCFA"));
        var createCourseCommandHandler = new CreateCourseCommandHandler(courseRepository);

        var result = createCourseCommandHandler.handle(createCourseCommand);

        var throwValue = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> createCourseCommandHandler.handle(createCourseCommand)
        );
        Assert.assertEquals("Course already exists", throwValue.getMessage());
    }
}
