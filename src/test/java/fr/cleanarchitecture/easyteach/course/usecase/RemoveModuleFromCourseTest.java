package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.RemoveModuleFromCourseCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.RemoveModuleFromCourseCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class RemoveModuleFromCourseTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private Course course;
    private Module module;

    @Before
    public void setUp() {
        course = new Course(
            "Course title",
            "Course description",
            new Teacher(),
            new Price(BigDecimal.ZERO, "FCFA")
        );
        module = new Module(
            "title",
            "description",
            1);
        course.addModule(module);
        courseRepository.save(course);
    }

    @Test
    public void shouldRemoveModuleFromCourseE2ETest() {
        var removeModuleFromCourseCommand = new RemoveModuleFromCourseCommand(
                course.getCourseId(), module.getModuleId());
        var removeModuleFromCourseCommandHandler = new RemoveModuleFromCourseCommandHandler(courseRepository);

        removeModuleFromCourseCommandHandler.handle(removeModuleFromCourseCommand);

        var updatedCourse = courseRepository.findByCourseId(course.getCourseId());

        Assert.assertTrue(updatedCourse.isPresent());
        Assert.assertEquals(0, updatedCourse.get().getModules().size());
    }


    @Test
    public void removeModuleFromNotExistsCourseTest_shouldThrowException() {
        var removeModuleFromCourseCommand = new RemoveModuleFromCourseCommand(
                "Garbage", module.getModuleId());
        var removeModuleFromCourseCommandHandler = new RemoveModuleFromCourseCommandHandler(courseRepository);

        Assert.assertThrows(
                "Course not found",
                NotFoundException.class,
                () -> removeModuleFromCourseCommandHandler.handle(removeModuleFromCourseCommand)
        );
    }

    @Test
    public void removeNotLinkedModuleToCourseTest_shouldThrowException() {
        var removeModuleFromCourseCommand = new RemoveModuleFromCourseCommand(
                course.getCourseId(), "Garbage");
        var removeModuleFromCourseCommandHandler = new RemoveModuleFromCourseCommandHandler(courseRepository);

        Assert.assertThrows(
                "Module not found",
                BadRequestException.class,
                () -> removeModuleFromCourseCommandHandler.handle(removeModuleFromCourseCommand)
        );
    }
}
