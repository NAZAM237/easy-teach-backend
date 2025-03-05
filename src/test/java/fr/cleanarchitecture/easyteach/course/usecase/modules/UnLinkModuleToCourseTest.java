package fr.cleanarchitecture.easyteach.course.usecase.modules;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.UnLinkModuleToCourseCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.UnLinkModuleToCourseCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryModuleRepository;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class UnLinkModuleToCourseTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private final ModuleRepository moduleRepository = new InMemoryModuleRepository();

    @Test
    public void unLinkModuleToCourseTest() {
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

        var unLinkModuleToCourseCommand = new UnLinkModuleToCourseCommand(course.getCourseId(), module.getModuleId());
        var unLinkModuleToCourseCommandHandler = new UnLinkModuleToCourseCommandHandler(courseRepository, moduleRepository);

        var result = unLinkModuleToCourseCommandHandler.handle(unLinkModuleToCourseCommand);

        Assert.assertNotNull(result);
        Assert.assertFalse(result.isLinkToCourse());
    }

    @Test
    public void unLinkModuleToNotExistCourseTest_shouldThrowException() {
        var module = new Module(
                "title",
                "description",
                1
        );
        moduleRepository.save(module);

        var unLinkModuleToCourseCommand = new UnLinkModuleToCourseCommand("Garbage", module.getModuleId());
        var unLinkModuleToCourseCommandHandler = new UnLinkModuleToCourseCommandHandler(courseRepository, moduleRepository);

        Assert.assertThrows(
                "Course not found",
                NotFoundException.class,
                () -> unLinkModuleToCourseCommandHandler.handle(unLinkModuleToCourseCommand)
        );
    }

    @Test
    public void unLinkNotLinkedModuleToCourseTest_shouldThrowException() {
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
        courseRepository.save(course);
        moduleRepository.save(module);

        var unLinkModuleToCourseCommand = new UnLinkModuleToCourseCommand(course.getCourseId(), module.getModuleId());
        var unLinkModuleToCourseCommandHandler = new UnLinkModuleToCourseCommandHandler(courseRepository, moduleRepository);

        Assert.assertThrows(
                "Module is not linked to any course",
                BadRequestException.class,
                () -> unLinkModuleToCourseCommandHandler.handle(unLinkModuleToCourseCommand)
        );
    }
}
