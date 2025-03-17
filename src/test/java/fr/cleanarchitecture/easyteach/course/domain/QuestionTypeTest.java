package fr.cleanarchitecture.easyteach.course.domain;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.course.domain.enums.QuestionType;
import org.junit.Assert;
import org.junit.Test;

public class QuestionTypeTest {

    @Test
    public void getQuestionTypeTest() {
        var type = "MULTIPLE_CHOICE";
        var questionType = QuestionType.getQuestionType(type);
        Assert.assertEquals(QuestionType.MULTIPLE_CHOICE, questionType);
    }

    @Test
    public void getNotExistQuestionType_shouldFailTest() {
        var type = "MULTIPLE";
        Assert.assertThrows(
                BadRequestException.class,
                () -> QuestionType.getQuestionType(type)
        );
    }
}
