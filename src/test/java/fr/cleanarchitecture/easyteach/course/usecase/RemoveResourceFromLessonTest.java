package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.FileFunctions;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.RemoveResourceFromLessonCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.RemoveResourceFromLessonCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.*;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryResourceFile;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.FileUploadProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.math.BigDecimal;

public class RemoveResourceFromLessonTest {

    private static final String UPLOAD_DIR = "/Users/nazam/Desktop/Projects/easy-teach/uploaded-resources";
    private static final String RESOURCE_URL = UPLOAD_DIR + "/documents/document.txt";

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private final FileUploadProperties properties = new FileUploadProperties();
    private final FileFunctions fileFunctions = new InMemoryResourceFile(properties);
    private Course course;
    private Module module;
    private Lesson lesson;
    private Resource resource;

    @Before
    public void init() {
        ReflectionTestUtils.setField(
                properties,
                "dir",
                UPLOAD_DIR
        );
    }

    @Before
    public void setUp() throws Exception {
        course = new Course(
                "courseTitle",
                "courseDescription",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA"));
        module = new Module("moduleTitle", "moduleDescription", 1);
        lesson = new Lesson("lessonTitle", ResourceType.IMAGES, null, "textContent", 1);
        resource = new Resource("resourceName", RESOURCE_URL);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
        lesson.addResource(resource);
        courseRepository.save(course);
    }

    @Test
    public void removeResourceFromLessonTest() {
        var removeResourceFromLessonCommand = new RemoveResourceFromLessonCommand(
                course.getCourseId(), module.getModuleId(), lesson.getLessonId(), resource.getResourceId());
        var removeResourceFromLessonCommandHandler = new RemoveResourceFromLessonCommandHandler(courseRepository, fileFunctions);

        removeResourceFromLessonCommandHandler.handle(removeResourceFromLessonCommand);

        Assert.assertTrue(lesson.getResources().isEmpty());
        Assert.assertFalse(new File(resource.getResourceUrl()).exists());
    }

    @Test
    public void removeNotExistResourceFromLesson_shouldThrowException() {
        var removeResourceFromLessonCommand = new RemoveResourceFromLessonCommand(
                course.getCourseId(), module.getModuleId(), lesson.getLessonId(), "Garbage");
        var removeResourceFromLessonCommandHandler = new RemoveResourceFromLessonCommandHandler(courseRepository, fileFunctions);

        var assertResult = Assert.assertThrows(
                NotFoundException.class,
                () -> removeResourceFromLessonCommandHandler.handle(removeResourceFromLessonCommand)
        );
        Assert.assertEquals("Resource not found", assertResult.getMessage());
    }

    @Test
    public void removeResourceWithInValidFileUrlFromLesson_shouldThrowException() {
        var removeResourceFromLessonCommand = new RemoveResourceFromLessonCommand(
                course.getCourseId(), module.getModuleId(), lesson.getLessonId(), resource.getResourceId());
        var removeResourceFromLessonCommandHandler = new RemoveResourceFromLessonCommandHandler(courseRepository, fileFunctions);

        var assertResult = Assert.assertThrows(
                BadRequestException.class,
                () -> removeResourceFromLessonCommandHandler.handle(removeResourceFromLessonCommand)
        );
        Assert.assertEquals("Unable to delete file " + RESOURCE_URL, assertResult.getMessage());
    }
}
