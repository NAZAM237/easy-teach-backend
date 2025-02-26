package fr.cleanarchitecture.easyteach.course.domain;

import fr.cleanarchitecture.easyteach.course.domain.enums.StatusEnum;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Instructor;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import org.apache.coyote.BadRequestException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class CourseTest {

    Course course = new Course(
            "course title",
            "course description",
            new Instructor(),
            new Price(BigDecimal.valueOf(1000), "FCFA")
    );

    @Test
    public void publishCourseTest() {
        course.publish();
        Assert.assertEquals(course.getStatus().name(), StatusEnum.PUBLISHED.name());
    }

    @Test
    public void archivedCourseWhenStatusIsPublishedTest() {
        course.publish();
        course.archive();
        Assert.assertEquals(course.getStatus().name(), StatusEnum.ARCHIVED.name());
    }

    @Test
    public void archivedCourseWhenStatusIsNotPublished_shouldFailTest() {
        var assertResult = Assert.assertThrows(
                IllegalStateException.class,
                course::archive
        );

        Assert.assertEquals("The status of the course is not published", assertResult.getMessage());
    }

    @Test
    public void restoreArchivedCourseTest() {
        course.publish();
        course.archive();
        course.restore();
        Assert.assertEquals(course.getStatus().name(), StatusEnum.DRAFT.name());
    }

    @Test
    public void restoreCourseWhenStatusIsNotArchived_shouldFailTest() {
        var assertResult = Assert.assertThrows(
                IllegalStateException.class,
                course::restore
        );

        Assert.assertEquals("The status of the course is not archived. You cannot restore it", assertResult.getMessage());
    }

    @Test
    public void addModuleToCourseTest() {
        Module module = new Module();
        course.addModule(module);
        Assert.assertFalse(course.getModules().isEmpty());
        Assert.assertEquals(1, course.getModules().size());
    }

    @Test
    public void removeModuleFromCourseTest() throws org.apache.coyote.BadRequestException {
        Module module = new Module();
        course.addModule(module);
        Assert.assertFalse(course.getModules().isEmpty());
        course.removeModule(module.getId());
        Assert.assertTrue(course.getModules().isEmpty());
    }

    @Test
    public void addModuleToCourseWhenModulePositionIsAlreadyInUse_shouldFailTest() {
        Module module = new Module(1);
        course.addModule(module);

        var assertResult = Assert.assertThrows(
                IllegalArgumentException.class,
                () -> course.addModule(module)
        );
        Assert.assertEquals("The module position already in use", assertResult.getMessage());
    }

    @Test
    public void removeNotExistingModuleFromCourse_shouldFailTest() {
        var assertResult = Assert.assertThrows(
                BadRequestException.class,
                (() -> course.removeModule("moduleId")
        ));
        Assert.assertEquals("The module does not exist", assertResult.getMessage());
    }

    @Test
    public void addModuleToArchivedCourse_shouldFailTest() {
        course.publish();
        course.archive();
        var assertResult = Assert.assertThrows(
                IllegalStateException.class,
                () -> course.addModule(new Module())
        );
        Assert.assertEquals("You cannot add module to archived course. Please restore course before!", assertResult.getMessage());
    }

    @Test
    public void showAllModulesTest() {
        Module module1 = new Module(1);
        Module module2 = new Module(2);
        Module module3 = new Module(3);
        course.addModule(module1);
        course.addModule(module2);
        course.addModule(module3);
        Assert.assertEquals(course.getModules().size(), 3);
    }
}
