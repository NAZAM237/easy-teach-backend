package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.RestoreCourseCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.RestoreCourseCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
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

public class RestoreCourseTest {

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private Course course;
    private Lesson lesson;

    @Before
    public void setUp() throws Exception {
        course = new Course(
                "Course title",
                "Course description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        lesson = new Lesson("lessonTitle", ResourceType.IMAGES, "Images", "textContent", 1);
    }

    @Test
    public void restoreCourseTest() {
        var module = new Module("moduleTitle", "moduleDescription",1);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
        course.publish();
        course.archive();
        courseRepository.save(course);

        var restoreCourseCommand = new RestoreCourseCommand(course.getCourseId());
        var restoreCourseCommandHandler = new RestoreCourseCommandHandler(courseRepository);

        var result = restoreCourseCommandHandler.handle(restoreCourseCommand);

        var archivedCourse = courseRepository.findByCourseId(course.getCourseId());

        Assert.assertTrue(archivedCourse.isPresent());
        Assert.assertEquals(result.getCourse().getStatus(), archivedCourse.get().getStatus());
    }

    @Test
    public void restoreUnExistingCourseTest_shouldThrowException() {
        var restoreCourseCommand = new RestoreCourseCommand(course.getCourseId());
        var restoreCourseCommandHandler = new RestoreCourseCommandHandler(courseRepository);
        Assert.assertThrows(
                "Course not found",
                NotFoundException.class,
                () -> restoreCourseCommandHandler.handle(restoreCourseCommand)
        );
    }

    @Test
    public void restoreNotArchivedCourseTest_shouldThrowException() {
        courseRepository.save(course);
        var restoreCourseCommand = new RestoreCourseCommand(course.getCourseId());
        var restoreCourseCommandHandler = new RestoreCourseCommandHandler(courseRepository);
        Assert.assertThrows(
                "The status of the course is not archived",
                BadRequestException.class,
                () -> restoreCourseCommandHandler.handle(restoreCourseCommand)
        );
    }
}
