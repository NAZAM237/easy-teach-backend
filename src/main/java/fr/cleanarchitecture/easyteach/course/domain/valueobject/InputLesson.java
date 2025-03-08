package fr.cleanarchitecture.easyteach.course.domain.valueobject;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;

public class InputLesson {
    private String title;
    private LessonType contentType;
    private String videoUrl;
    private String textContent;
    private int order;

    public InputLesson() {}

    public InputLesson(String title, String contentType, String videoUrl, String textContent, int order) {
        this.title = title;
        this.contentType = convertToLessonType(contentType);
        this.videoUrl = videoUrl;
        this.textContent = textContent;
        this.order = order;
    }

    public InputLesson(String title, String contentType, String videoUrl, String textContent) {
        this.title = title;
        this.contentType = convertToLessonType(contentType);
        this.videoUrl = videoUrl;
        this.textContent = textContent;
    }

    public String getTitle() {
        return title;
    }

    public LessonType getContentType() {
        return contentType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getTextContent() {
        return textContent;
    }

    public int getOrder() {
        return order;
    }

    private LessonType convertToLessonType(String lessonType) {
        return switch (lessonType) {
            case "TEXT" -> LessonType.TEXT;
            case "VIDEO" -> LessonType.VIDEO;
            case "AUDIO" -> LessonType.AUDIO;
            case "DOCUMENT" -> LessonType.DOCUMENT;
            default -> throw new BadRequestException("Invalid lessonType " + lessonType);
        };
    }
}
