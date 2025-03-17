package fr.cleanarchitecture.easyteach.course.infrastructure.spring.mapper;

import fr.cleanarchitecture.easyteach.course.domain.model.Answer;
import fr.cleanarchitecture.easyteach.course.domain.model.Question;
import fr.cleanarchitecture.easyteach.course.domain.model.Quiz;
import fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos.QuizDto;

import java.util.stream.Collectors;

public class MapQuizDtoToQuiz {

    public static Quiz execute(QuizDto quizDto) {
        var questions = quizDto.getQuestions().stream().map(
                questionDto -> new Question(
                        questionDto.getQuestionText(),
                        questionDto.getQuestionType(),
                        questionDto.getAnswers().stream().map(
                                answerDto -> new Answer(
                                        answerDto.getAnswerText(),
                                        answerDto.isCorrect()
                                )
                        ).collect(Collectors.toList())
                )
        ).collect(Collectors.toSet());

        return new Quiz(
                quizDto.getQuizTitle(),
                quizDto.getDescription(),
                questions,
                quizDto.getPassingScore()
        );
    }
}
