package fr.cleanarchitecture.easyteach.course.domain.enums;

import fr.cleanarchitecture.easyteach.shared.domain.exceptions.BadRequestException;

public enum QuestionType {
    MULTIPLE_CHOICE, SINGLE_CHOICE, TEXT;

    public static QuestionType getQuestionType(String questionType) {
        return switch (questionType.toLowerCase()) {
            case "multiple_choice" -> MULTIPLE_CHOICE;
            case "single_choice" -> SINGLE_CHOICE;
            case "text" -> TEXT;
            default -> throw new BadRequestException("QuestionType " + questionType + " not supported");
        };
    }
}
