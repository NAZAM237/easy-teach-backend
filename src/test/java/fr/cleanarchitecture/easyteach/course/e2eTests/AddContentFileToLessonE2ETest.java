package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class AddContentFileToLessonE2ETest extends EasyTeachIntegrationTests {

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
        MockMultipartFile pdfFile = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "PDF content".getBytes()
        );

        mockMvc.perform(multipart(
                    HttpMethod.POST,
                    "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/content",
                    course.getCourseId(), module.getModuleId(), lesson.getLessonId())
                    .file(pdfFile)
                    .param("type", "DOCUMENTS"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.fileName").value("file.pdf"))
            .andExpect(jsonPath("$.data.fileType").value("application/pdf"))
            .andExpect(jsonPath("$.data.fileSize").value(11))
            .andExpect(jsonPath("$.data.filePath").value(lesson.getContentFileUrl()));
    }
}
