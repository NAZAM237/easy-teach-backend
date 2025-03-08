package fr.cleanarchitecture.easyteach.course.usecase.modules;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.RemoveLessonFromModuleCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.module.RemoveLessonFromModuleCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class RemoveLessonFromModuleTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private Module module;
    private Lesson lesson;
    private Course course;

    @Before
    public void setUp() throws Exception {
        course = new Course(
            "courseTitle",
            "courseDescription",
            new Teacher(),
            new Price(BigDecimal.ZERO, "FCFA")
        );
        module = new Module("Introduction à JAVA", "Description", 1);
        lesson = new Lesson("Introduction", LessonType.TEXT, null, null, 1);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
        courseRepository.save(course);
    }

    @Test
    public void removeLessonFromModuleTest() {
        var removeLessonFromModuleCommand = new RemoveLessonFromModuleCommand(
                course.getCourseId(), module.getModuleId(), lesson.getLessonId());
        var removeLessonFromModuleCommandHandler = new RemoveLessonFromModuleCommandHandler(courseRepository);

        removeLessonFromModuleCommandHandler.handle(removeLessonFromModuleCommand);

        Assert.assertTrue(module.getLessons().isEmpty());
    }

    @Test
    public void removeNotExistLessonFromModuleTest() {
        var removeLessonFromModuleCommand = new RemoveLessonFromModuleCommand(course.getCourseId(), module.getModuleId(), "FakeLessonId");
        var removeLessonFromModuleCommandHandler = new RemoveLessonFromModuleCommandHandler(courseRepository);

        Assert.assertThrows(
                "Lesson not found",
                NotFoundException.class,
                () -> removeLessonFromModuleCommandHandler.handle(removeLessonFromModuleCommand)
        );
    }
}
