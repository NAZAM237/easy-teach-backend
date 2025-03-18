package fr.cleanarchitecture.easyteach.course.domain;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.core.domain.exceptions.NotFoundException;
import fr.cleanarchitecture.easyteach.course.domain.enums.CourseStatus;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Course;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Module;
import fr.cleanarchitecture.easyteach.course.domain.model.Teacher;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.Price;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

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
        var module = new Module("moduleTitle", "moduleDescription", 1);
        course.addModule(module);
        var lesson = new Lesson("lessonTitle", ResourceType.IMAGES, "Images", "textContext", 1);
        course.addLessonToModule(module.getModuleId(), lesson);
        course.publish();
        Assert.assertEquals(course.getStatus().name(), CourseStatus.PUBLISHED.name());
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
    public void publishCourseWithoutLessonContent_shouldFailTest() {
        var module = new Module("moduleTitle", "moduleDescription", 1);
        course.addModule(module);
        var lesson = new Lesson("lessonTitle", ResourceType.IMAGES, null, "textContent", 1);
        course.addLessonToModule(module.getModuleId(), lesson);
        var assertResult = Assert.assertThrows(
                BadRequestException.class,
                () -> course.publish()
        );
        Assert.assertEquals(
                "ContentFile must not be null for " + lesson.getContentType().name(),
                assertResult.getMessage());
    }

    @Test
    public void publishCourseWithoutContentLessons_shouldFailTest() {
        course.addModule(new Module("moduleTitle", "moduleDescription", 1));
        var assertResult = Assert.assertThrows(
                BadRequestException.class,
                () -> course.publish()
        );
        Assert.assertEquals("You must provide at least one lesson", assertResult.getMessage());
    }

    @Test
    public void archivedCourseWhenStatusIsPublishedTest() {
        var module = new Module("moduleTitle", "moduleDescription",1);
        course.addModule(module);
        course.addLessonToModule(
                module.getModuleId(),
                new Lesson("lessonTitle", ResourceType.IMAGES, "Images", "textContext", 1)
        );
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
        var module = new Module("moduleTitle", "moduleDescription",1);
        course.addModule(module);
        course.addLessonToModule(
                module.getModuleId(),
                new Lesson("lessonTitle", ResourceType.IMAGES, "Images", "textContext", 1)
        );
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
        Module module = new Module("moduleTitle", "moduleDescription",1);
        course.addModule(module);
        Assert.assertFalse(course.getModules().isEmpty());
        Assert.assertEquals(1, course.getModules().size());
    }

    @Test
    public void removeModuleFromCourseTest() throws org.apache.coyote.BadRequestException {
        Module module = new Module("moduleTitle", "moduleDescription",1);
        course.addModule(module);
        Assert.assertFalse(course.getModules().isEmpty());
        course.removeModule(module.getModuleId());
        Assert.assertTrue(course.getModules().isEmpty());
    }

    @Test
    public void addModuleToCourseWhenModulePositionIsAlreadyInUse_shouldFailTest() {
        Module module = new Module("moduleTitle", "moduleDescription",1);
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
                NotFoundException.class,
                (() -> course.removeModule("moduleId")
        ));
        Assert.assertEquals("Module not found", assertResult.getMessage());
    }

    @Test
    public void addModuleToArchivedCourse_shouldFailTest() {
        var module = new Module("moduleTitle", "moduleDescription",1);
        course.addModule(module);
        course.addLessonToModule(
                module.getModuleId(),
                new Lesson("lessonTitle", ResourceType.IMAGES, "Images", "textContext", 1)
        );
        course.publish();
        course.archive();
        var assertResult = Assert.assertThrows(
                BadRequestException.class,
                () -> course.addModule(new Module("moduleTitle", "moduleDescription",2))
        );
        Assert.assertEquals("You cannot add module to archived course. Please restore course before!", assertResult.getMessage());
    }

    @Test
    public void showAllModulesTest() {
        Module module1 = new Module("moduleTitle", "moduleDescription",1);
        Module module2 = new Module("moduleTitle", "moduleDescription",2);
        Module module3 = new Module("moduleTitle", "moduleDescription",3);
        course.addModule(module1);
        course.addModule(module2);
        course.addModule(module3);
        Assert.assertEquals(course.getModules().size(), 3);
    }

    @Test
    public void changeDataCourseTest() {
        course.updateCourseData(
                "Title2",
                "Description2",
                new Price(BigDecimal.valueOf(500), "FCFA"));
        Assert.assertEquals("Title2", course.getCourseTitle());
        Assert.assertEquals("Description2", course.getCourseDescription());
        Assert.assertEquals(BigDecimal.valueOf(500), course.getPrice().getAmount());
    }

    @Test
    public void addLessonToModuleTest() {
        var module = new Module("moduleTitle", "moduleDescription", 1);
        var lesson = new Lesson("lessonTitle", ResourceType.IMAGES, null, "textContent", 1);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
        var updatedModule = course.getModules().stream()
                .filter(module1 -> module1.getModuleId().equals(module.getModuleId()))
                .findFirst()
                .orElseThrow();
        Assert.assertEquals(1, updatedModule.getLessons().size());
    }

    @Test
    public void addLessonToNotExistModule_shouldFailTest() {
        var lesson = new Lesson("lessonTitle", ResourceType.IMAGES, null, "textContent", 1);
        Assert.assertThrows(
                "Module not found",
                NotFoundException.class,
                () -> course.addLessonToModule("FakeModuleId", lesson)
        );
    }

    @Test
    public void removeLessonFromCourseTest() {
        var module = new Module("moduleTitle", "moduleDescription", 1);
        var lesson = new Lesson("lessonTitle", ResourceType.IMAGES, null, "textContent", 1);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson);
        course.removeLessonToModule(module.getModuleId(), lesson.getLessonId());
        var updatedModule = course.getModules().stream()
                .filter(module1 -> module1.getModuleId().equals(module.getModuleId()))
                .findFirst()
                .orElseThrow();
        Assert.assertTrue(updatedModule.getLessons().isEmpty());
    }

    @Test
    public void removeLessonToNotExistModule_shouldFailTest() {
        var lesson = new Lesson("lessonTitle", ResourceType.IMAGES, null, "textContent", 1);
        Assert.assertThrows(
                "Module not found",
                NotFoundException.class,
                () -> course.removeLessonToModule("FakeModuleId", lesson.getLessonId())
        );
    }

    @Test
    public void removeNotExistLessonToModule_shouldFailTest() {
        var module = new Module("moduleTitle", "moduleDescription", 1);
        course.addModule(module);
        Assert.assertThrows(
                "The lesson not found",
                NotFoundException.class,
                () -> course.removeLessonToModule(module.getModuleId(), "FakeLessonId")
        );
    }

    @Test
    public void reorderLessonsFromModuleTest() {
        var module = new Module("Programmation Java", "Description", 1);
        var lesson1 = new Lesson("Introduction", ResourceType.IMAGES, null, "Intro", 1);
        var lesson2 = new Lesson("Variables", ResourceType.IMAGES, null, "Les types", 2);
        var lesson3 = new Lesson("Boucles", ResourceType.IMAGES, null, "Les boucles", 3);
        course.addModule(module);
        course.addLessonToModule(module.getModuleId(), lesson1);
        course.addLessonToModule(module.getModuleId(), lesson2);
        course.addLessonToModule(module.getModuleId(), lesson3);

        course.reorderLessonsFromModule(module.getModuleId(), List.of(lesson3, lesson1, lesson2));
        Assert.assertEquals(1, lesson3.getOrder());
        Assert.assertEquals(2, lesson1.getOrder());
        Assert.assertEquals(3, lesson2.getOrder());
    }

    @Test
    public void updateModuleDataFromCourseTest() {
        var module = new Module("Programmation Java", "Description", 1);
        course.addModule(module);
        course.updateModuleData(module.getModuleId(), "Programmation", "Description2");
        var updatedModule = course.getModules().stream()
                .filter(module1 -> module1.getModuleId().equals(module.getModuleId()))
                .findFirst().orElseThrow();
        Assert.assertEquals("Programmation", updatedModule.getModuleTitle());
        Assert.assertEquals("Description2", updatedModule.getModuleDescription());
    }

    @Test
    public void reorderModulesFromCourseTest() {
        var module = new Module("Introduction à Java", "Description", 1);
        var module2 = new Module("Les conditions", "Description", 2);
        var module3 = new Module("Les boucles", "Description", 3);
        course.addModule(module);
        course.addModule(module2);
        course.addModule(module3);

        course.reorderModules(List.of(module2, module3, module));

        Assert.assertEquals(1, module2.getOrder());
        Assert.assertEquals(2, module3.getOrder());
        Assert.assertEquals(3, module.getOrder());
    }

    @Test
    public void reorderWithInvalidModulesList_shouldFailTest() {
        var module = new Module("Introduction à Java", "Description", 1);
        var module2 = new Module("Les conditions", "Description", 2);
        var module3 = new Module("Les boucles", "Description", 3);
        var module4 = new Module("Les fonctions", "Description", 4);
        course.addModule(module);
        course.addModule(module2);
        course.addModule(module3);

        var exception = Assert.assertThrows(
                BadRequestException.class,
                () -> course.reorderModules(List.of(module2, module3, module4))
        );
        Assert.assertEquals("Invalid modules list", exception.getMessage());
    }

    @Test
    public void reorderWithInCompleteModulesList_shouldFailTest() {
        var module = new Module("Introduction à Java", "Description", 1);
        var module2 = new Module("Les conditions", "Description", 2);
        var module3 = new Module("Les boucles", "Description", 3);
        course.addModule(module);
        course.addModule(module2);
        course.addModule(module3);

        var exception = Assert.assertThrows(
                BadRequestException.class,
                () -> course.reorderModules(List.of(module2, module3))
        );
        Assert.assertEquals("Invalid modules list", exception.getMessage());
    }
}
