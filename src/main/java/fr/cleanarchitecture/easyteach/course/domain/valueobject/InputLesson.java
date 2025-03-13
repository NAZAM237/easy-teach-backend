package fr.cleanarchitecture.easyteach.course.domain.valueobject;

import fr.cleanarchitecture.easyteach.course.domain.enums.ResourceType;

public class InputLesson {
    private String title;
    private ResourceType contentType;
    private String videoUrl;
    private String textContent;
    private int order;

    public InputLesson() {}

    public InputLesson(String title, String contentType, String videoUrl, String textContent, int order) {
        this.title = title;
        this.contentType = ResourceType.getResourceType(contentType);
        this.videoUrl = videoUrl;
        this.textContent = textContent;
        this.order = order;
    }

    public InputLesson(String title, String contentType, String videoUrl, String textContent) {
        this.title = title;
        this.contentType = ResourceType.getResourceType(contentType);
        this.videoUrl = videoUrl;
        this.textContent = textContent;
    }

    public String getTitle() {
        return title;
    }

    public ResourceType getContentType() {
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
