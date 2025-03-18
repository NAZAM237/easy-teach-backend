package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.FileFunctions;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AddResourceToLessonCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.AddResourceToLessonCommandHandler;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.domain.viewmodel.IdsCourse;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryCourseRepository;
import fr.cleanarchitecture.easyteach.course.infrastructure.persistence.inmemory.InMemoryResourceFile;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.configuration.FileUploadPropertiesConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

public class AddResourceToLessonTest {

    private static final String UPLOAD_DIR = "/Users/nazam/Desktop/Projects/easy-teach/uploaded-resources";

    private final CourseRepository courseRepository = new InMemoryCourseRepository();
    private final FileUploadPropertiesConfiguration properties = new FileUploadPropertiesConfiguration();
    private final FileFunctions fileFunctions = new InMemoryResourceFile(properties);
    private Course course;
    private Module module;
    private Lesson lesson;

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
    }

    @Test
    public void addVideoResourceToLessonTest() {
        var idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId());
        MockMultipartFile videoFile = new MockMultipartFile(
                "file",
                "test.mp4",
                "video/mp4",
                "Video content".getBytes()
        );
        var addResourceToLessonCommand = new AddResourceToLessonCommand(idsCourse, videoFile, "VIDEOS");
        var addResourceToLessonCommandHandler = new AddResourceToLessonCommandHandler(courseRepository, fileFunctions);

        addResourceToLessonCommandHandler.handle(addResourceToLessonCommand);

        var course = courseRepository.findByCourseId(idsCourse.getCourseId()).orElseThrow();
        var resources = course.getModules()
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
        var newResource = resources.get(0);
        Assert.assertEquals(
                UPLOAD_DIR + "/videos/" + newResource.getResourceName(),
                newResource.getResourceUrl());
    }

    @Test
    public void addAudioResourceToLessonTest() {
        var idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId());
        MockMultipartFile audioFile = new MockMultipartFile(
                "audio",
                "test.mp3",
                "audio/mp3",
                "Audio content".getBytes()
        );
        var addResourceToLessonCommand = new AddResourceToLessonCommand(idsCourse, audioFile, "AUDIOS");
        var addResourceToLessonCommandHandler = new AddResourceToLessonCommandHandler(courseRepository, fileFunctions);

        addResourceToLessonCommandHandler.handle(addResourceToLessonCommand);

        var course = courseRepository.findByCourseId(idsCourse.getCourseId()).orElseThrow();
        var resources = course.getModules()
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
        var newResource = resources.get(0);
        Assert.assertEquals(
                UPLOAD_DIR + "/audios/" + newResource.getResourceName(),
                newResource.getResourceUrl());
    }

    @Test
    public void addDocumentResourceToLessonTest() {
        var idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId());
        MockMultipartFile textFile = new MockMultipartFile(
                "document",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Text content".getBytes()
        );
        var addResourceToLessonCommand = new AddResourceToLessonCommand(idsCourse, textFile, "DOCUMENTS");
        var addResourceToLessonCommandHandler = new AddResourceToLessonCommandHandler(courseRepository, fileFunctions);

        addResourceToLessonCommandHandler.handle(addResourceToLessonCommand);

        var course = courseRepository.findByCourseId(idsCourse.getCourseId()).orElseThrow();
        var resources = course.getModules()
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
        var newResource = resources.get(0);
        Assert.assertEquals(
                UPLOAD_DIR + "/documents/" + newResource.getResourceName(),
                newResource.getResourceUrl());
    }

    @Test
    public void addImageResourceToLessonTest() {
        var idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId());
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                "Image content".getBytes()
        );
        var addResourceToLessonCommand = new AddResourceToLessonCommand(idsCourse, imageFile, "IMAGES");
        var addResourceToLessonCommandHandler = new AddResourceToLessonCommandHandler(courseRepository, fileFunctions);

        addResourceToLessonCommandHandler.handle(addResourceToLessonCommand);

        var course = courseRepository.findByCourseId(idsCourse.getCourseId()).orElseThrow();
        var resources = course.getModules()
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
        var newResource = resources.get(0);
        Assert.assertEquals(
                UPLOAD_DIR + "/images/" + newResource.getResourceName(),
                newResource.getResourceUrl());
    }

    @Test
    public void addInvalidCategoryResourceToLessonTest_shouldThrowException() {
        var idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId());
        MockMultipartFile videoFile = new MockMultipartFile(
                "file",
                "test.mp4",
                "video/mp4",
                "Video content".getBytes()
        );
        var addResourceToLessonCommand = new AddResourceToLessonCommand(idsCourse, videoFile, "VIEW");
        var addResourceToLessonCommandHandler = new AddResourceToLessonCommandHandler(courseRepository, fileFunctions);

        var resultThrow = Assert.assertThrows(
                BadRequestException.class,
                () -> addResourceToLessonCommandHandler.handle(addResourceToLessonCommand)
        );
        Assert.assertEquals("Resource type not recognized", resultThrow.getMessage());
    }

    @Test
    public void addInvalidFileExtensionResourceToLessonTest_shouldThrowException() {
        var idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId());
        MockMultipartFile videoFile = new MockMultipartFile(
                "file",
                "test.mp11",
                "video/mp4",
                "Video content".getBytes()
        );
        var addResourceToLessonCommand = new AddResourceToLessonCommand(idsCourse, videoFile, "VIDEOS");
        var addResourceToLessonCommandHandler = new AddResourceToLessonCommandHandler(courseRepository, fileFunctions);

        var resultThrow = Assert.assertThrows(
                BadRequestException.class,
                () -> addResourceToLessonCommandHandler.handle(addResourceToLessonCommand)
        );
        Assert.assertEquals(
                "Invalid file extension for category: " + addResourceToLessonCommand.getResourceType(),
                resultThrow.getMessage());
    }
}
