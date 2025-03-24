package fr.cleanarchitecture.easyteach.course.domain.viewmodel;

public class IdsCourse {
    private String courseId;
    private String moduleId;
    private String lessonId;
    private String questionId;
    private String answerId;

    public IdsCourse(String courseId, String moduleId, String lessonId) {
        this.courseId = courseId;
        this.moduleId = moduleId;
        this.lessonId = lessonId;
    }

    public IdsCourse(String courseId, String moduleId, String lessonId, String questionId) {
        this.courseId = courseId;
        this.moduleId = moduleId;
        this.lessonId = lessonId;
        this.questionId = questionId;
    }

    public IdsCourse(String courseId, String moduleId, String lessonId, String questionId, String answerId) {
        this.courseId = courseId;
        this.moduleId = moduleId;
        this.lessonId = lessonId;
        this.questionId = questionId;
        this.answerId = answerId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getAnswerId() {
        return answerId;
    }
}
