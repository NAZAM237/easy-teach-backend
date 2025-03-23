package fr.cleanarchitecture.easyteach.course.infrastructure.spring.mapper;

import fr.cleanarchitecture.easyteach.course.domain.model.Answer;
import fr.cleanarchitecture.easyteach.course.domain.model.Question;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos.QuestionDto;

import java.util.stream.Collectors;

public class FromQuestionDtoToQuestion {

    public static Question execute(QuestionDto dto) {
        var answers = dto.getAnswers().stream()
                .map(answerDto -> new Answer(answerDto.getAnswerText(), answerDto.isCorrect()))
                .collect(Collectors.toList());
        return new Question(dto.getQuestionText(), dto.getQuestionType(), answers);
    }
}
