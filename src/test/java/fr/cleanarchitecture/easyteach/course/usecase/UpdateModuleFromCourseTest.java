package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.UpdateModuleFromCourseCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.UpdateModuleFromCourseCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class UpdateModuleFromCourseTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private Module module;
    private Course course;

    @Before
    public void setUp() throws Exception {
        course = new Course(
            "courseTitle",
            "courseDescription",
            new Teacher(),
            new Price(BigDecimal.ZERO, "FCFA")
        );
        module = new Module("Introduction à la programmation Java", "Une description", 1);
        course.addModule(module);
    }

    @Test
    public void updateModuleFromCourseTest() {
        courseRepository.save(course);

        var updateModuleCommand = new UpdateModuleFromCourseCommand(
                course.getCourseId(),
                module.getModuleId(),
                "Introduction",
                "Desc");
        var updateModuleCommandHandler = new UpdateModuleFromCourseCommandHandler(courseRepository);

        updateModuleCommandHandler.handle(updateModuleCommand);

        var updatedCourse = courseRepository.findByCourseId(course.getCourseId()).orElseThrow();
        var updatedModule = updatedCourse.getModules()
                .stream()
                .filter(module1 -> module1.getModuleId().equals(module.getModuleId()))
                .findFirst()
                .orElseThrow();

        Assert.assertEquals(updateModuleCommand.getModuleTitle(), updatedModule.getModuleTitle());
        Assert.assertEquals(updateModuleCommand.getModuleDescription(), updatedModule.getModuleDescription());
    }

    @Test
    public void updateNotFoundModuleTest_shouldThrowException() {
        var updateModuleCommand = new UpdateModuleFromCourseCommand(
                course.getCourseId(),
                "Garbage",
                "Introduction",
                "Desc");
        var updateModuleCommandHandler = new UpdateModuleFromCourseCommandHandler(courseRepository);
        Assert.assertThrows(
                "Module not found",
                NotFoundException.class,
                () -> updateModuleCommandHandler.handle(updateModuleCommand)
        );
    }
}
