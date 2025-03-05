package fr.cleanarchitecture.easyteach.course.usecase.modules;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.LinkModuleToCourseCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.LinkModuleToCourseCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryModuleRepository;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class LinkModuleToCourseTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private final ModuleRepository moduleRepository = new InMemoryModuleRepository();

    @Test
    public void linkModuleToCourseTest() {
        var course = new Course(
                "title",
                "description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        courseRepository.save(course);
        var module = new Module(
                "title",
                "description",
                1
        );
        moduleRepository.save(module);

        var linkModuleToCourseCommand = new LinkModuleToCourseCommand(course.getCourseId(), module.getModuleId());
        var linkModuleToCourseCommandHandler = new LinkModuleToCourseCommandHandler(courseRepository, moduleRepository);
        var result = linkModuleToCourseCommandHandler.handle(linkModuleToCourseCommand);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getModuleId(), module.getModuleId());
        Assert.assertTrue(result.isLinkToCourse());
    }

    @Test
    public void linkModuleAlwaysLinkedToCourseTest_shouldThrowException() {
        var course = new Course(
                "title",
                "description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        var module = new Module(
                "title",
                "description",
                1
        );
        course.addModule(module);
        moduleRepository.save(module);
        courseRepository.save(course);

        var linkModuleToCourseCommand = new LinkModuleToCourseCommand(course.getCourseId(), module.getModuleId());
        var linkModuleToCourseCommandHandler = new LinkModuleToCourseCommandHandler(courseRepository, moduleRepository);

        Assert.assertThrows(
                "Module is always linked in a course",
                BadRequestException.class,
                () -> linkModuleToCourseCommandHandler.handle(linkModuleToCourseCommand)
        );
    }

    @Test
    public void linkModuleToNotExistCourseTest_shouldThrowException() {
        var course = new Course(
                "title",
                "description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        var module = new Module(
                "title",
                "description",
                1
        );
        moduleRepository.save(module);

        var linkModuleToCourseCommand = new LinkModuleToCourseCommand(course.getCourseId(), module.getModuleId());
        var linkModuleToCourseCommandHandler = new LinkModuleToCourseCommandHandler(courseRepository, moduleRepository);

        Assert.assertThrows(
                "Course not found",
                NotFoundException.class,
                () -> linkModuleToCourseCommandHandler.handle(linkModuleToCourseCommand)
        );
    }
}
