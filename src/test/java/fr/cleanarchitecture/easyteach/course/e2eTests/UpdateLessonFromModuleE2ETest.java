package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.UpdateLessonFromModuleDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class UpdateLessonFromModuleE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

    private Course course;
    private Module module;
    private Lesson lesson;

    @Before
    public void setUp() {
        course = new Course(
                "courseTitle",
                "courseDescription",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        module = new Module("moduleTitle", "moduleDescription", 1);
        lesson = new Lesson("lessonTitle", ResourceType.IMAGES, null, "textContent", 1);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
        courseRepository.save(course);
    }

    @Test
    public void updateLessonFromModuleE2ETest() throws Exception {
        var updateLessonFromModuleDto = new UpdateLessonFromModuleDto(
                "lessonTitle",
                "AUDIO",
                null,
                null);
        mockMvc
            .perform(
                    MockMvcRequestBuilders.patch(
                        "/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}",
                        course.getCourseId(),
                        module.getModuleId(),
                        lesson.getLessonId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(updateLessonFromModuleDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        var updatedCourse = courseRepository.findByCourseId(course.getCourseId()).orElseThrow();
        var updatedModule = updatedCourse.getModules()
                .stream()
                .filter(m -> m.getModuleId().equals(module.getModuleId()))
                .findFirst()
                .orElseThrow();
        var updatedLesson = updatedModule.getLessons()
                .stream().filter(l -> l.getLessonId().equals(lesson.getLessonId()))
                .findFirst()
                .orElseThrow();

        Assert.assertEquals(updateLessonFromModuleDto.getLessonTitle(), updatedLesson.getLessonTitle());
        Assert.assertEquals(updateLessonFromModuleDto.getLessonType(), updatedLesson.getContentType().name());
    }
}
