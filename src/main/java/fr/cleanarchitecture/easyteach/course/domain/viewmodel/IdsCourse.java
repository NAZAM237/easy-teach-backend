package fr.cleanarchitecture.easyteach.course.domain.viewmodel;

public class IdsCourse {
    private String courseId;
    private String moduleId;
    private String lessonId;

    public IdsCourse(String courseId, String moduleId, String lessonId) {
        this.courseId = courseId;
        this.moduleId = moduleId;
        this.lessonId = lessonId;
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
}
