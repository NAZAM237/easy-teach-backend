package fr.cleanarchitecture.easyteach.course.infrastructure.spring;

public class UpdateLessonFromModuleDto {
    private String lessonTitle;
    private String lessonType;
    private String videoUrl;
    private String lessonTextContent;

    public UpdateLessonFromModuleDto() {}

    public UpdateLessonFromModuleDto(String lessonTitle, String lessonType, String videoUrl, String lessonTextContent) {
        this.lessonTitle = lessonTitle;
        this.lessonType = lessonType;
        this.videoUrl = videoUrl;
        this.lessonTextContent = lessonTextContent;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public String getLessonType() {
        return lessonType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getLessonTextContent() {
        return lessonTextContent;
    }
}
