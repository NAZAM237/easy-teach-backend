package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.GetAllCoursesCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.GetAllCoursesCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class GetAllCoursesTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();

    @Test
    public void getAllCoursesTest() {
        var course1 = new Course(
                "title1",
                "description1",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        var course2 = new Course(
                "title2",
                "description2",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        courseRepository.save(course1);
        courseRepository.save(course2);

        var getAllCoursesCommand = new GetAllCoursesCommand();
        var getAllCourseCommandHandler = new GetAllCoursesCommandHandler(courseRepository);

        var result = getAllCourseCommandHandler.handle(getAllCoursesCommand);

        Assert.assertFalse(result.getData().isEmpty());
        Assert.assertEquals(2, result.getData().size());
    }

    @Test
    public void getAllCoursesShouldBeEmpty() {
        var getAllCoursesCommand = new GetAllCoursesCommand();
        var getAllCourseCommandHandler = new GetAllCoursesCommandHandler(courseRepository);

        var result = getAllCourseCommandHandler.handle(getAllCoursesCommand);

        Assert.assertTrue(result.getData().isEmpty());
    }
}
