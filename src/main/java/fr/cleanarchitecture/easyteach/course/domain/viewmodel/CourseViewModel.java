package fr.cleanarchitecture.easyteach.course.domain.viewmodel;

public class CourseViewModel {
    private String courseId;
    private String message;

    public CourseViewModel() {}

    public CourseViewModel(String courseId, String message) {
        this.courseId = courseId;
        this.message = message;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getMessage() {
        return message;
    }
}
