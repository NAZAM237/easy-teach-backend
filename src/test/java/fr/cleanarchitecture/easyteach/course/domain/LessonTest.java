package fr.cleanarchitecture.easyteach.course.domain;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.model.Resource;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.InputLesson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LessonTest {
    private Lesson lesson;

    @Before
    public void setUp() {
        lesson = new Lesson(
            "lessonTitle",
            ResourceType.IMAGES,
            null,
            "textContent",
            1
        );
    }

    @Test
    public void updateDataTest() {
        var inputLesson = new InputLesson("lessonTitle2", "AUDIOS", "Audios", null);
        lesson.updateDate(inputLesson);
        Assert.assertEquals(inputLesson.getTitle(), lesson.getLessonTitle());
        Assert.assertEquals(inputLesson.getContentType(), lesson.getContentType());
        Assert.assertNull(lesson.getTextContent());
    }

    @Test
    public void updateDataIfTypeDifferentTextAndContentUrlNull_shouldFailTest() {
        var inputLesson = new InputLesson("lessonTitle2", "AUDIOS", null, null);
        Assert.assertThrows(
                BadRequestException.class,
                () -> lesson.updateDate(inputLesson)
        );
    }

    @Test
    public void addResourceToLessonTest() {
        var resource = new Resource("resourceName", "resourceUrl");
        lesson.addResource(resource);
        Assert.assertEquals(1, lesson.getResources().size());
        Assert.assertEquals("resourceName", lesson.getResources().get(0).getResourceName());
        Assert.assertEquals("resourceUrl", lesson.getResources().get(0).getResourceUrl());
    }

    @Test
    public void removeResourceFromLessonTest() {
        var resource = new Resource("resourceName", "resourceUrl");
        lesson.addResource(resource);
        Assert.assertEquals(1, lesson.getResources().size());
        Assert.assertEquals("resourceName", lesson.getResources().get(0).getResourceName());
        lesson.removeResource(resource.getResourceId());
        Assert.assertTrue(lesson.getResources().isEmpty());
    }
}
