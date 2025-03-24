package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.*;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class AddResourceToLessonE2ETest extends EasyTeachIntegrationTests {

    private static final String UPLOAD_DIR = "/Users/nazam/Desktop/Projects/easy-teach/uploaded-resources";

    @Autowired
    private CourseRepository courseRepository;

    private Course course;
    private Module module;
    private Lesson lesson;

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
    public void shouldAddResourceToLessonTest() throws Exception {
        MockMultipartFile pdfFile = createFile();

        mockMvc.perform(multipart(
                    HttpMethod.POST,
                    "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/resources",
                    course.getCourseId(), module.getModuleId(), lesson.getLessonId())
                        .file(pdfFile)
                        .param("type", "DOCUMENTS"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.resourceUrl").value(UPLOAD_DIR + "/documents/file.pdf"));
    }

    @Test
    public void addResourceWithNoFileToLessonTest_shouldThrowException() throws Exception {
        mockMvc.perform(multipart(
                        HttpMethod.POST,
                        "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/resources",
                        course.getCourseId(), module.getModuleId(), lesson.getLessonId()))
                .andExpect(status().isBadRequest());
    }



    private MockMultipartFile createFile() {
        return new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "PDF content".getBytes()
        );
    }

    private List<Resource> getResources(Course course) {
        return course.getModules()
                .stream()
                .filter(module1 -> module1.getModuleId().equals(module.getModuleId()))
                .findFirst()
                .orElseThrow()
                .getLessons()
                .stream()
                .filter(lesson1 -> lesson1.getLessonId().equals(lesson.getLessonId()))
                .findFirst()
                .orElseThrow()
                .getResources();
    }
}
