package fr.cleanarchitecture.easyteach.course.infrastructure.spring.dtos;

public class UpdateLessonFromModuleDto {
    private String lessonTitle;
    private String lessonType;
    private String contentFileUrl;
    private String lessonTextContent;

    public UpdateLessonFromModuleDto() {}

    public UpdateLessonFromModuleDto(String lessonTitle, String lessonType, String contentFileUrl, String lessonTextContent) {
        this.lessonTitle = lessonTitle;
        this.lessonType = lessonType;
        this.contentFileUrl = contentFileUrl;
        this.lessonTextContent = lessonTextContent;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public String getLessonType() {
        return lessonType;
    }

    public String getContentFileUrl() {
        return contentFileUrl;
    }

    public String getLessonTextContent() {
        return lessonTextContent;
    }
}
