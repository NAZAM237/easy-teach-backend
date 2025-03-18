package fr.cleanarchitecture.easyteach.course.domain.model;

import fr.cleanarchitecture.easyteach.core.domain.exceptions.BadRequestException;
import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;
import fr.cleanarchitecture.easyteach.course.domain.valueobject.InputLesson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lesson {
    private String lessonId;
    private String lessonTitle;
    private ResourceType contentType;
    private String contentFileUrl;
    private String textContent;
    private int order;
    private List<Resource> resources = new ArrayList<>();
    private Quiz quiz;

    public Lesson() {}

    public Lesson(String lessonTitle, ResourceType contentType, String contentFileUrl, String textContent, int order) {
        this.lessonId = UUID.randomUUID().toString();
        this.lessonTitle = lessonTitle;
        this.contentType = contentType;
        this.contentFileUrl = contentFileUrl;
        this.textContent = textContent;
        this.order = order;
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public ResourceType getContentType() {
        return contentType;
    }

    public String getContentFileUrl() {
        return contentFileUrl;
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

    public void updateDate(InputLesson lesson) {
        if (!ResourceType.TEXT.equals(lesson.getContentType()) && null == lesson.getContentFileUrl()) {
            throw new BadRequestException("contentFile must not be null for " + lesson.getContentType().name());
        }
        this.lessonTitle = lesson.getTitle();
        this.contentType = lesson.getContentType();
        this.contentFileUrl = lesson.getContentFileUrl();
        this.textContent = lesson.getTextContent();
    }

    public void addResource(Resource resource) {
        this.resources.add(resource);
    }

    public void removeResource(String resourceId) {
        this.resources.removeIf(resource -> resource.getResourceId().equals(resourceId));
    }

    public void attachQuiz(Quiz quiz) {
        if (this.quiz != null) {
            throw new BadRequestException("Quiz is already attached");
        }
        this.quiz = quiz;
    }
}
