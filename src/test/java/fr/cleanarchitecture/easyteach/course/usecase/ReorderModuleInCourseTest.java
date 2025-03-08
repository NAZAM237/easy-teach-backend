package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.ReorderModuleInCourseCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.ReorderModuleInCourseCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class ReorderModuleInCourseTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private Course course;
    private Module module;
    private Module module2;
    private Module module3;

    @Before
    public void setUp() {
        course = new Course(
                "Programmation JAVA",
                "Description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA"));
        module = new Module("Introduction à Java", "Description", 1);
        module2 = new Module("Les conditions", "Description", 2);
        module3 = new Module("Les boucles", "Description", 3);
        course.addModule(module);
        course.addModule(module2);
        course.addModule(module3);
        courseRepository.save(course);
    }

    @Test
    public void reorderModuleInCourseTest() {
        var newOrderList = List.of(module2, module3, module);
        var reorderModuleInCourseCommand = new ReorderModuleInCourseCommand(course.getCourseId(), newOrderList);
        var reorderModuleInCourseCommandHandler = new ReorderModuleInCourseCommandHandler(courseRepository);

        reorderModuleInCourseCommandHandler.handle(reorderModuleInCourseCommand);

        Assert.assertEquals(1, module2.getOrder());
        Assert.assertEquals(3, module.getOrder());
        Assert.assertEquals(2, module3.getOrder());
    }
}
