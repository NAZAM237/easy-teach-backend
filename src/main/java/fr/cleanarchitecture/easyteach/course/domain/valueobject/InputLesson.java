package fr.cleanarchitecture.easyteach.course.domain.valueobject;

import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;

public class InputLesson {
    private String title;
    private LessonType contentType;
    private String videoUrl;
    private String textContent;
    private int order;

    public InputLesson() {}

    public InputLesson(String title, LessonType contentType, String videoUrl, String textContent, int order) {
        this.title = title;
        this.contentType = contentType;
        this.videoUrl = videoUrl;
        this.textContent = textContent;
        this.order = order;
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
}
