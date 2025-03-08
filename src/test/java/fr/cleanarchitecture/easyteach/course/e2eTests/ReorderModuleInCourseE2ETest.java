package fr.cleanarchitecture.easyteach.course.e2eTests;

import fr.cleanarchitecture.easyteach.EasyTeachIntegrationTests;
import fr.cleanarchitecture.easyteach.course.application.ports.CourseRepository;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class ReorderModuleInCourseE2ETest extends EasyTeachIntegrationTests {

    @Autowired
    private CourseRepository courseRepository;
    private Course course;
    private Module module;
    private Module module2;
    private Module module3;

    @Before
    public void setUp() {
        course = new Course(
                "Programmation JAVA",
                "Description",
                new Teacher(),
                new Price(BigDecimal.ZERO, "FCFA"));
        module = new Module("Introduction à Java", "Description", 1);
        module2 = new Module("Les conditions", "Description", 2);
        module3 = new Module("Les boucles", "Description", 3);
        course.addModule(module);
        course.addModule(module2);
        course.addModule(module3);
        courseRepository.save(course);
    }

    @Test
    public void test() {}

    /*@Test
    public void reorderModuleInCourseE2ETest() throws Exception {
        var newOrderedList = List.of(module3, module, module2);
        mockMvc
            .perform(MockMvcRequestBuilders.patch("/courses/{courseId}/modules", course.getCourseId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(newOrderedList)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Assert.assertEquals(1, module3.getOrder());
        Assert.assertEquals(2, module.getOrder());
        Assert.assertEquals(3, module2.getOrder());
    }*/
}
