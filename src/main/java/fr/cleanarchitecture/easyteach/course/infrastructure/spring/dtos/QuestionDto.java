package fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos;

import fr.cleanarchitecture.easyteach.course.domain.enums.QuestionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class QuestionDto {
    private String questionText;
    private QuestionType questionType;
    private List<AnswerDto> answers = new ArrayList<>();

    public QuestionDto(String questionText, String questionType, List<AnswerDto> answers) {
        this.questionText = questionText;
        this.questionType = QuestionType.getQuestionType(questionType);
        answers = answers;
    }
}
