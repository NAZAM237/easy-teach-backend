package fr.cleanarchitecture.easyteach.course.domain.viewmodel;

import java.time.LocalDateTime;
import java.util.Locale;

public class CourseViewModel {
    private String courseId;
    private String message;
    private LocalDateTime updatedAt;

    public CourseViewModel(String courseId, String message, LocalDateTime updatedAt) {
        this.courseId = courseId;
        this.message = message;
        this.updatedAt = updatedAt;
    }

    public String getCourseId() {
        return courseId;
    }
}
