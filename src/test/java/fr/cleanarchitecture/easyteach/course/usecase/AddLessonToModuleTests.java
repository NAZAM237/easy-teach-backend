package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AddLessonToModuleCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.AddLessonToModuleCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.InputLesson;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.shared.domain.exceptions.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class AddLessonToModuleTests {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private Course course;
    private Module module;
    private InputLesson inputLesson;

    @Before
    public void setUp() {
        inputLesson = new InputLesson("title", "IMAGES", "videoUrl", "textContent", 1);

        course = new Course(
                "courseTitle",
                "courseDescription",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        module = new Module("Introduction à JAVA", "Cours complet", 1);
        course.addModule(module);
    }

    @Test
    public void addLessonToModuleTest() {
        courseRepository.save(course);

        var addLessonToModuleCommand = new AddLessonToModuleCommand(
                course.getCourseId(),
                module.getModuleId(),
                inputLesson);
        var addLessonToModuleCommandHandler = new AddLessonToModuleCommandHandler(courseRepository);

        var result = addLessonToModuleCommandHandler.handle(addLessonToModuleCommand);

        Assert.assertEquals(inputLesson.getContentType(), result.getData().getContentType());
        Assert.assertEquals(inputLesson.getTitle(), result.getData().getLessonTitle());
        Assert.assertEquals(inputLesson.getOrder(), result.getData().getOrder());
    }

    @Test
    public void addLessonToNotExistModuleTest() {
        var addLessonToModuleCommand = new AddLessonToModuleCommand(
                course.getCourseId(),
                "Garbage",
                inputLesson);
        var addLessonToModuleCommandHandler = new AddLessonToModuleCommandHandler(courseRepository);

        Assert.assertThrows(
                "Module not found",
                NotFoundException.class,
                () -> addLessonToModuleCommandHandler.handle(addLessonToModuleCommand)
        );
    }

    @Test
    public void addLessonToModuleWhenPositionAlreaydInUseTest_shouldThrowException() {
        var lesson = new Lesson("title", ResourceType.IMAGES, "videoUrl", "textContent", 1);
        course.addLessonToModule(module.getModuleId(), lesson);
        courseRepository.save(course);

        var addLessonToModuleCommand = new AddLessonToModuleCommand(
                course.getCourseId(),
                module.getModuleId(),
                inputLesson);
        var addLessonToModuleCommandHandler = new AddLessonToModuleCommandHandler(courseRepository);

        Assert.assertThrows(
                "Lesson position already in use",
                BadRequestException.class,
                () -> addLessonToModuleCommandHandler.handle(addLessonToModuleCommand)
        );
    }
}
