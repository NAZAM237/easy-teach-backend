package fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
    private String quizTitle;
    private String quizDescription;
    private Set<QuestionDto> questions;
    private int passingScore;
}
