package fr.cleanarchitecture.easyteach.course.usecase;

import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.application.ports.FileFunctions;
import fr.cleanarchitecture.easyteach.course.application.usecases.commands.AddContentFileToLessonCommand;
import fr.cleanarchitecture.easyteach.course.application.usecases.handlers.AddContentFileToLessonCommandHandler;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

public class AddContentFileToLessonTest {

    private static final String UPLOAD_DIR = "/Users/nazam/Desktop/Projects/easy-teach/uploaded-lesson";

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
                "lessonContentFileFolder",
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
    public void addContentFileToLessonTest() {
        var idsCourse = new IdsCourse(course.getCourseId(), module.getModuleId(), lesson.getLessonId());
        var pdfFile =  createMockFile();
        var addContentFileToLessonCommand = new AddContentFileToLessonCommand(idsCourse, pdfFile, "DOCUMENTS");
        var addContentFileToLessonCommandHandler = new AddContentFileToLessonCommandHandler(courseRepository, fileFunctions);

        var result = addContentFileToLessonCommandHandler.handle(addContentFileToLessonCommand);

        var course = courseRepository.findByCourseId(idsCourse.getCourseId()).orElseThrow();
        var updatedLesson = course.getModules()
                .stream()
                .filter(module1 -> module1.getModuleId().equals(idsCourse.getModuleId()))
                .findFirst()
                .orElseThrow()
                .getLessons()
                .stream()
                .filter(lesson1 -> lesson1.getLessonId().equals(idsCourse.getLessonId()))
                .findFirst()
                .orElseThrow();

        Assert.assertEquals(
                UPLOAD_DIR + "/documents/" + result.getFileName(),
                updatedLesson.getContentFileUrl());
    }

    private MockMultipartFile createMockFile() {
        return new MockMultipartFile(
                "file",
                "test.pdf",
                "DOCUMENTS",
                "Pdf content".getBytes());
    }
}
