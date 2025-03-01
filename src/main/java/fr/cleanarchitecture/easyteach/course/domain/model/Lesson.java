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
}
