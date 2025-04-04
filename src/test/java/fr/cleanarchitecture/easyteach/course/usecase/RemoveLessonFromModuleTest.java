package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.RemoveLessonFromModuleCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.RemoveLessonFromModuleCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;
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
        lesson = new Lesson("Introduction", ResourceType.IMAGES, null, null, 1);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
        courseRepository.save(course);
    }

    @Test
    public void removeLessonFromModuleTest() {
        var removeLessonFromModuleCommand = new RemoveLessonFromModuleCommand(
                course.getCourseId(), module.getModuleId(), lesson.getLessonId());
        var removeLessonFromModuleCommandHandler = createHandler();

        removeLessonFromModuleCommandHandler.handle(removeLessonFromModuleCommand);

        Assert.assertTrue(module.getLessons().isEmpty());
    }

    @Test
    public void removeNotExistLessonFromModuleTest() {
        var removeLessonFromModuleCommand = new RemoveLessonFromModuleCommand(course.getCourseId(), module.getModuleId(), "FakeLessonId");
        var removeLessonFromModuleCommandHandler = createHandler();

        Assert.assertThrows(
                "Lesson not found",
                NotFoundException.class,
                () -> removeLessonFromModuleCommandHandler.handle(removeLessonFromModuleCommand)
        );
    }

    private RemoveLessonFromModuleCommandHandler createHandler() {
        return new RemoveLessonFromModuleCommandHandler(courseRepository);
    };
}
