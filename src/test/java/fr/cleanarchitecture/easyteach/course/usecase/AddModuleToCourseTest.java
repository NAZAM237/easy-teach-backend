package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AddModuleToCourseCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.AddModuleToCourseCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class AddModuleToCourseTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private Course course;

    @Before
    public void setUp() {
        course = new Course(
                "Course title",
                "Course description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
    }

    @Test
    public void addModuleToCourseTest() {
        courseRepository.save(course);

        var addModuleToCourseCommand = new AddModuleToCourseCommand(
                course.getCourseId(), "courseTitle", "courseDescription", 1);
        var addModuleToCourseCommandHandler = new AddModuleToCourseCommandHandler(courseRepository);

        addModuleToCourseCommandHandler.handle(addModuleToCourseCommand);

        var updatedCourse = courseRepository.findByCourseId(course.getCourseId());

        Assert.assertTrue(updatedCourse.isPresent());
        Assert.assertEquals(1, updatedCourse.get().getModules().size());
    }

    @Test
    public void addModuleToNotExistsCourseTest_shouldThrowException() {
        var addModuleToCourseCommand = new AddModuleToCourseCommand(
                course.getCourseId(), "courseTitle", "courseDescription", 1);
        var addModuleToCourseCommandHandler = new AddModuleToCourseCommandHandler(courseRepository);

        Assert.assertThrows(
                "Course not found",
                NotFoundException.class,
                () -> addModuleToCourseCommandHandler.handle(addModuleToCourseCommand)
        );
    }
}
