package fr.cleanarchitecture.easyteach.course.domain;

import fr.cleanarchitecture.easyteach.course.domain.enums.CourseStatus;
import org.junit.Assert;
import org.junit.Test;

public class StatusTest {

    private final static String DRAFT_STATUS = "DRAFT";

    @Test
    public void fromStringToEnumTest() {
        var status = DRAFT_STATUS;
        var result = CourseStatus.fromStringToEnum(status);
        Assert.assertEquals(result, CourseStatus.DRAFT);
    }

    @Test
    public void fromStringToEnumWithInvalidValue_shouldThrowException() {
        var status = "";
        Assert.assertThrows(
                IllegalArgumentException.class,
                () -> CourseStatus.fromStringToEnum(status));
    }

    @Test
    public void fromEnumToStringTest() {
        var status = CourseStatus.DRAFT;
        var result = CourseStatus.fromEnumToString(status);
        Assert.assertEquals(result, DRAFT_STATUS);
    }

    @Test
    public void fromEnumToStringWithInvalidValue_shouldThrowException() {
        var status = "";
        Assert.assertThrows(
                IllegalArgumentException.class,
                () -> CourseStatus.fromStringToEnum(status));
    }
}
