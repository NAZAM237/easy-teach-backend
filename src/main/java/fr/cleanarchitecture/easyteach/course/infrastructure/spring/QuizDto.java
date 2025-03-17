package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
    private String quizTitle;
    private String description;
    private Set<QuestionDto> questions;
    private int passingScore;
}
