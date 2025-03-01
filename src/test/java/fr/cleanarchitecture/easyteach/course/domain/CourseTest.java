package fr.cleanarchitecture.easyteach.course.domain;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.course.domain.enums.CourseStatus;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class CourseTest {

    Course course = new Course(
            "course title",
            "course description",
            new Teacher("teacher id",
                    "teacher name",
                    "teacher biography",
                    "teacher email",
                    "teacher phone",
                    "teacher photo"),
            new Price(BigDecimal.valueOf(1000), "FCFA")
    );

    @Test
    public void publishCourseTest() {
        course.getModules().add(new Module(1));
        course.publish();
        Assert.assertEquals(course.getStatus().name(), CourseStatus.PUBLISHED.name());
    }

    @Test
    public void archivedCourseWhenStatusIsPublishedTest() {
        course.getModules().add(new Module(1));
        course.publish();
        course.archive();
        Assert.assertEquals(course.getStatus().name(), CourseStatus.ARCHIVED.name());
    }

    @Test
    public void archivedCourseWhenStatusIsNotPublished_shouldFailTest() {
        var assertResult = Assert.assertThrows(
                BadRequestException.class,
                course::archive
        );

        Assert.assertEquals("The status of the course is not published", assertResult.getMessage());
    }

    @Test
    public void restoreArchivedCourseTest() {
        course.getModules().add(new Module(1));
        course.publish();
        course.archive();
        course.restore();
        Assert.assertEquals(course.getStatus().name(), CourseStatus.DRAFT.name());
    }

    @Test
    public void restoreCourseWhenStatusIsNotArchived_shouldFailTest() {
        var assertResult = Assert.assertThrows(
                BadRequestException.class,
                course::restore
        );

        Assert.assertEquals("The status of the course is not archived. You cannot restore it", assertResult.getMessage());
    }

    @Test
    public void addModuleToCourseTest() {
        Module module = new Module(1);
        course.addModule(module);
        Assert.assertFalse(course.getModules().isEmpty());
        Assert.assertEquals(1, course.getModules().size());
    }

    @Test
    public void removeModuleFromCourseTest() throws org.apache.coyote.BadRequestException {
        Module module = new Module(1);
        course.addModule(module);
        Assert.assertFalse(course.getModules().isEmpty());
        course.removeModule(module.getModuleId());
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
        Assert.assertEquals("The module not found", assertResult.getMessage());
    }

    @Test
    public void addModuleToArchivedCourse_shouldFailTest() {
        course.getModules().add(new Module(1));
        course.publish();
        course.archive();
        var assertResult = Assert.assertThrows(
                BadRequestException.class,
                () -> course.addModule(new Module(2))
        );
        Assert.assertEquals("You cannot add module to archived course. Please restore course before!", assertResult.getMessage());
    }

    @Test
    public void publishCourseWithoutModules_shouldFailTest() {
        var assertResult = Assert.assertThrows(
                BadRequestException.class,
                () -> course.publish()
        );
        Assert.assertEquals("You must provide at least one module", assertResult.getMessage());
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

    @Test
    public void changeCourseTitleTest() {
        course.changeTitle("title2");
        Assert.assertEquals("title2", course.getCourseTitle());
    }

    @Test
    public void changeCourseDescriptionTest() {
        course.changeDescription("Description2");
        Assert.assertEquals("Description2", course.getCourseDescription());
    }

    @Test
    public void changeCoursePriceTest() {
        course.changePrice(new Price(BigDecimal.valueOf(500), "FCFA"));
        Assert.assertEquals(BigDecimal.valueOf(500), course.getPrice().getAmount());
    }
}
