package fr.cleanarchitecture.easyteach.course.e2eTests.modules;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.ModuleRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
public class RemoveLessonFromModuleE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private ModuleRepository moduleRepository;

    private Lesson lesson;
    private Module module;

    @Before
    public void setUp() {
        module = new Module("Introduction à JAVA", "Description", 1);
        lesson = new Lesson("Introduction", LessonType.TEXT, null, null, 1);
        module.addLesson(lesson);
    }

    @Test
    public void shouldRemoveLessonFromModuleE2ETest() throws Exception {
        moduleRepository.save(module);
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/modules/{moduleId}/remove-lesson-from-module", module.getModuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lesson.getLessonId())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void removeLessonFromNotExistModuleE2ETest_shouldThrowException() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.patch("/modules/{moduleId}/remove-lesson-from-module", module.getModuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lesson.getLessonId())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
