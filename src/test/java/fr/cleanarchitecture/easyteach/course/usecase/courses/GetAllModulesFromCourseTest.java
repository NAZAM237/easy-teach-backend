package fr.cleanarchitecture.easyteach.course.usecase.courses;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.course.GetAllModulesFromCourseCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.course.GetAllModulesFromCourseCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class GetAllModulesFromCourseTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();

    @Test
    public void getAllModulesFromCourseTest() {
        var course = new Course(
                "title",
                "description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        course.addModule(new Module("title", "description", 1));
        course.addModule(new Module("title2", "description2", 2));
        course.addModule(new Module("titl3", "description", 3));
        courseRepository.save(course);

        var getAllModulesFromCourseCommand = new GetAllModulesFromCourseCommand(course.getCourseId());
        var getAllModulesFromCourseCommandHandler = new GetAllModulesFromCourseCommandHandler(courseRepository);

        var result = getAllModulesFromCourseCommandHandler.handle(getAllModulesFromCourseCommand);

        Assert.assertNotNull(result);
        Assert.assertEquals(course.getCourseId(), result.getCourseId());
        Assert.assertEquals(3, result.getModules().size());
    }

    @Test
    public void getAllModulesFromNotExistingCourseTest_shouldThrowException() {
        var getAllModulesFromCourseCommand = new GetAllModulesFromCourseCommand("courseId");
        var getAllModulesFromCourseCommandHandler = new GetAllModulesFromCourseCommandHandler(courseRepository);

        Assert.assertThrows(
                "Course not found",
                NotFoundException.class,
                () -> getAllModulesFromCourseCommandHandler.handle(getAllModulesFromCourseCommand)
        );
    }
}
