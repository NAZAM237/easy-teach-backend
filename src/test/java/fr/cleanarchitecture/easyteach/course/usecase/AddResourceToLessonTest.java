package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.FileFunctions;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AddResourceToLessonCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.AddResourceToLessonCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.*;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryResourceFile;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.configuration.FileUploadPropertiesConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

public class AddResourceToLessonTest {

    private static final String UPLOAD_DIR = "/Users/nazam/Desktop/Projects/easy-teach/uploaded-resources";

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private final FileUploadPropertiesConfiguration properties = new FileUploadPropertiesConfiguration();
    private final FileFunctions fileFunctions = new InMemoryResourceFile(properties);
    private Course course;
    private Module module;
    private Lesson lesson;
    private IdsCourse idsCourse;

    @Before
    public void init() {
        ReflectionTestUtils.setField(
                properties,
                "resourceFolder",
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
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
        courseRepository.save(course);
        idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId());
    }

    @Test
    public void addVideoResourceToLessonTest() {
        MockMultipartFile videoFile = createMockFile("test.mp4", "video/mp4");
        var addResourceToLessonCommand = new AddResourceToLessonCommand(idsCourse, videoFile, "VIDEOS");
        var addResourceToLessonCommandHandler = createHandler();

        addResourceToLessonCommandHandler.handle(addResourceToLessonCommand);

        var course = courseRepository.findByCourseId(idsCourse.getCourseId()).orElseThrow();
        var resources = getResources(course);
        var newResource = resources.get(0);
        Assert.assertEquals(
                UPLOAD_DIR + "/videos/" + newResource.getResourceName(),
                newResource.getResourceUrl());
    }

    @Test
    public void addAudioResourceToLessonTest() {
        MockMultipartFile audioFile = createMockFile("tset.mp3", "audio/mp3");
        var addResourceToLessonCommand = new AddResourceToLessonCommand(idsCourse, audioFile, "AUDIOS");
        var addResourceToLessonCommandHandler = createHandler();

        addResourceToLessonCommandHandler.handle(addResourceToLessonCommand);

        var course = courseRepository.findByCourseId(idsCourse.getCourseId()).orElseThrow();
        var resources = getResources(course);
        var newResource = resources.get(0);
        Assert.assertEquals(
                UPLOAD_DIR + "/audios/" + newResource.getResourceName(),
                newResource.getResourceUrl());
    }

    @Test
    public void addDocumentResourceToLessonTest() {
        var idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId());
        MockMultipartFile textFile = createMockFile("test.txt", "text/plain");
        var addResourceToLessonCommand = new AddResourceToLessonCommand(idsCourse, textFile, "DOCUMENTS");
        var addResourceToLessonCommandHandler = createHandler();

        addResourceToLessonCommandHandler.handle(addResourceToLessonCommand);

        var course = courseRepository.findByCourseId(idsCourse.getCourseId()).orElseThrow();
        var resources = getResources(course);
        var newResource = resources.get(0);
        Assert.assertEquals(
                UPLOAD_DIR + "/documents/" + newResource.getResourceName(),
                newResource.getResourceUrl());
    }

    @Test
    public void addImageResourceToLessonTest() {
        MockMultipartFile imageFile = createMockFile("test.png", "image/png");
        var addResourceToLessonCommand = new AddResourceToLessonCommand(idsCourse, imageFile, "IMAGES");
        var addResourceToLessonCommandHandler = createHandler();

        addResourceToLessonCommandHandler.handle(addResourceToLessonCommand);

        var course = courseRepository.findByCourseId(idsCourse.getCourseId()).orElseThrow();
        var resources = getResources(course);
        var newResource = resources.get(0);
        Assert.assertEquals(
                UPLOAD_DIR + "/images/" + newResource.getResourceName(),
                newResource.getResourceUrl());
    }

    @Test
    public void addInvalidCategoryResourceToLessonTest_shouldThrowException() {
        MockMultipartFile videoFile = createMockFile("test.mp4", "video/mp4");
        var addResourceToLessonCommand = new AddResourceToLessonCommand(idsCourse, videoFile, "VIEW");
        var addResourceToLessonCommandHandler = createHandler();

        var resultThrow = Assert.assertThrows(
                BadRequestException.class,
                () -> addResourceToLessonCommandHandler.handle(addResourceToLessonCommand)
        );
        Assert.assertEquals("Resource type not recognized", resultThrow.getMessage());
    }

    @Test
    public void addInvalidFileExtensionResourceToLessonTest_shouldThrowException() {
        MockMultipartFile videoFile = createMockFile("test.mp11", "video/mp4");

        var addResourceToLessonCommand = new AddResourceToLessonCommand(idsCourse, videoFile, "VIDEOS");
        var addResourceToLessonCommandHandler = createHandler();

        var resultThrow = Assert.assertThrows(
                BadRequestException.class,
                () -> addResourceToLessonCommandHandler.handle(addResourceToLessonCommand)
        );
        Assert.assertEquals(
                "Invalid file extension for category: " + addResourceToLessonCommand.getResourceType(),
                resultThrow.getMessage());
    }

    private MockMultipartFile createMockFile(String originalFileName, String contentType) {
        return new MockMultipartFile(
                "file",
                originalFileName,
                contentType,
                "File content".getBytes()
        );
    }

    private AddResourceToLessonCommandHandler createHandler() {
        return new AddResourceToLessonCommandHandler(courseRepository, fileFunctions);
    }

    private List<Resource> getResources(Course course) {
        return course.getModules()
                .stream()
                .filter(module1 -> module1.getModuleId().equals(idsCourse.getModuleId()))
                .findFirst()
                .orElseThrow()
                .getLessons()
                .stream()
                .filter(lesson1 -> lesson1.getLessonId().equals(idsCourse.getLessonId()))
                .findFirst()
                .orElseThrow()
                .getResources();
    }
}
