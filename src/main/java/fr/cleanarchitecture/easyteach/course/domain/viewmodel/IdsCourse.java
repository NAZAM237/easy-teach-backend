package fr.cleanarchitecture.easyteach.course.domain.viewmodel;

public class IdsCourse {
    private String courseId;
    private String moduleId;
    private String lessonId;
    private String quizId;

    public IdsCourse(String courseId, String moduleId, String lessonId) {
        this.courseId = courseId;
        this.moduleId = moduleId;
        this.lessonId = lessonId;
    }

    public IdsCourse(String courseId, String moduleId, String lessonId, String quizId) {
        this.courseId = courseId;
        this.moduleId = moduleId;
        this.lessonId = lessonId;
        this.quizId = quizId;
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

    public String getQuizId() {
        return quizId;
    }
}
