package fr.cleanarchitecture.easyteach.course.domain;

import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;
import fr.cleanarchitecture.easyteach.course.domain.model.Lesson;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.InputLesson;
import org.junit.Assert;
import org.junit.Test;

public class LessonTest {

    @Test
    public void updateDataTest() {
        Lesson lesson = new Lesson(
                "lessonTitle",
                LessonType.TEXT,
                null,
                "textContent",
                1
        );
        var inputLesson = new InputLesson("lessonTitle2", "AUDIO", null, null);
        lesson.updateDate(inputLesson);
        Assert.assertEquals(inputLesson.getTitle(), lesson.getLessonTitle());
        Assert.assertEquals(inputLesson.getContentType(), lesson.getContentType());
        Assert.assertNull(lesson.getTextContent());
    }
}
