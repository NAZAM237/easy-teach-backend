package fr.cleanarchitecture.easyteach.course.domain.model;

import fr.cleanarchitecture.easyteach.course.domain.enums.LessonType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lesson {
    private String lessonId;
    private String lessonTitle;
    private LessonType contentType;
    private String videoUrl;
    private String textContent;
    private int order;
    private List<Resource> resources = new ArrayList<>();
    private Quiz quiz;

    public Lesson() {}

    public Lesson(String lessonTitle, LessonType contentType, String videoUrl, String textContent, int order) {
        this.lessonId = UUID.randomUUID().toString();
        this.lessonTitle = lessonTitle;
        this.contentType = contentType;
        this.videoUrl = videoUrl;
        this.textContent = textContent;
        this.order = order;
        Quiz quiz = new Quiz();
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getLessonTitle() {
        return lessonTitle;
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

    public List<Resource> getResources() {
        return resources;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void changeOrder(int newOrder) {
        this.order = newOrder;
    }
}
