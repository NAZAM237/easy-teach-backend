package fr.cleanarchitecture.easyteach.course.e2eTests.modules;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.ReorderLessonToModuleDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class ReorderLessonToModuleE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;

    private Course course;
    private Module module;
    private Lesson lesson1;
    private Lesson lesson2;
    private Lesson lesson3;

    @Before
    public void setUp() {
        course = new Course(
                "courseTitle",
                "courseDescription",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA")
        );
        module = new Module("Programmation Java", "Description", 1);
        lesson1 = new Lesson("Introduction", LessonType.TEXT, null, "Intro", 1);
        lesson2 = new Lesson("Variables", LessonType.TEXT, null, "Les types", 2);
        lesson3 = new Lesson("Boucles", LessonType.TEXT, null, "Les boucles", 3);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson1);
        course.addLessonToModule(module.getModuleId(), lesson2);
        course.addLessonToModule(module.getModuleId(), lesson3);
        courseRepository.save(course);
    }

    //TODO: A compléter
    /*@Test
    public void shouldReorderLessonsInModuleE2ETest() throws Exception {
        var lessons = Arrays.asList(lesson3, lesson1, lesson2);
        var dto = new ReorderLessonToModuleDto(lessons);
        var result = mockMvc
                        .perform(MockMvcRequestBuilders.patch("/modules/{moduleId}/lessons", module.getModuleId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(dto)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        var moduleViewModel = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ModuleViewModel.class
        );

        Assert.assertNotNull(moduleViewModel);
        Assert.assertEquals(1, lesson3.getOrder());
        Assert.assertEquals(2, lesson1.getOrder());
        Assert.assertEquals(3, lesson2.getOrder());
    }*/

    @Test
    public void reOrderLessonIfLessonNotInModuleE2ETest_shouldThrowException() throws Exception {
        Lesson lesson4 = new Lesson("Exceptions", LessonType.TEXT, null, "Gestion des erreurs", 4);
        List<Lesson> invalidOrder = Arrays.asList(lesson3, lesson1, lesson4);
        var dto = new ReorderLessonToModuleDto(invalidOrder);
        mockMvc
                .perform(MockMvcRequestBuilders.patch(
                        "/courses/{courseId}/modules/{moduleId}/lessons",
                                course.getCourseId(),
                                module.getModuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void reOrderLessonIfLessonsListIsIncompleteE2ETest_shouldThrowException() throws Exception {
        List<Lesson> incompleteOrder = Arrays.asList(lesson1, lesson2);
        var dto = new ReorderLessonToModuleDto(incompleteOrder);
        mockMvc
                .perform(MockMvcRequestBuilders.patch(
                        "/courses/{courseId}/modules/{moduleId}/lessons",
                                course.getCourseId(),
                                module.getModuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
